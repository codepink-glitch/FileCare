<template>
  <div>
    <header-component/>
    <div v-show="loading" style="z-index: 1000; position:absolute; left:53%; top:43%; transform: translate(-50%, 0);"><div class="lds-heart" style="width: 160px; height: 160px;"><div></div></div></div>
    <transition>
      <popup-component @closePopup="closePopup" v-show="popupShow" :class="loading ? 'popup-loading' : ''">
        <div v-if="popupType === 'folder'" class="popup_inner_content">
          <span>New directory name: </span>
          <input v-model="newDirectoryName"
                  class="text-areas"
                  @change="newDirectoryNameChanged"/>
          <button @click="createDirectory" class="create-button">Create</button>
        </div>
        <div v-else-if="popupType === 'file'" class="popup_inner_content">
          <drop-zone-component @drop.prevent="drop" @change="selectedFile" />
          <div style="margin-top: 10px">
            <span>File: {{dropzoneFile.name}}</span>
            <button class="upload-button" style="float: right" @click="uploadFile">Upload</button>
          </div>
        </div>
      </popup-component>
    </transition>
    <div class="centerDiv rounded-border" style="padding-top: 0;">
      <table>
        <tr>
          <th class="th-bottom-border buttons-div-position">
            <img @click="showCreatePopup('folder')" class="cursor-pointer" style="float: right" width="28" height="28" src = "../assets/icons/add_folder.svg"/>
            <img @click="showCreatePopup('file')" class="cursor-pointer" style="float: right" width="28" height="28" src="../assets/icons/add_file.svg"/>
          </th>
        </tr>
        <tr v-for="(file) in filesArr" :key="file.path">
          <th class="th-bottom-border">
            <div>
<!--            <div :class="file.extension === 'directory' || file.name === '..' ? 'cursor-pointer' : ''">-->
              <div :class="file.extension === 'directory' || file.name === '..' ? 'cursor-pointer' : ''"
                   style="display: inline-block;" @click="changeFolder(file)">
                <img v-if="file.extension === 'directory'" width="28" height="28" src="../assets/icons/folder.svg" />
                <img v-else-if="file.extension" width="28" height="28" src="../assets/icons/file.svg" />
                <span style="vertical-align: super">{{ file.name }}</span>
              </div>
              <div class="buttons-div-position" style="display: inline-block;">
                <img v-show="!!file.extension && file.extension !== 'directory'" class="cursor-pointer" width="27" height="27" src="../assets/icons/download.svg"/>
                <img v-show="file.name !== '..'" @click="deleteFile(file)" class="cursor-pointer" width="27" height="27" src="../assets/icons/delete.svg"/>
              </div>
            </div>
          </th>
        </tr>
      </table>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
import api from "@/api";
import PopupComponent from "@/components/PopupComponent.vue";
import {FileDetails, PopupType} from "@/interfaces";
import DropZoneComponent from "@/components/DropZoneComponent.vue";

export default defineComponent ({
  name: "BrowseView",
  components: {DropZoneComponent, PopupComponent, HeaderComponent},
  data() {
    return {
      filesArr: [{name: "..", extension: "directory"}],
      loading: false,
      popupShow: false,
      currentFolder: "",
      newDirectoryName: "",
      newDirectoryNameValid: true,
      popupType: PopupType.CREATE_FOLDER,
    }
  },
  setup() {
    let dropzoneFile: any = ref("")

    const drop = (e: any) => {
      dropzoneFile.value = e.dataTransfer.files[0]
    }

    const selectedFile = () => {
      const input = document.querySelector(".dropzone-file") as HTMLInputElement
      if (input !== null && input.files !== null) {
        dropzoneFile.value = input.files[0]
      }
    }

    return { dropzoneFile, drop, selectedFile }
  },
  mounted() {
    this.fetchFolder()
  },
  methods: {
    uploadFile() {
      if (this.dropzoneFile.name) {
        this.loading = true
        const data: FormData = new FormData()
        data.append("file", this.dropzoneFile)
        data.append("folderDetails",
          JSON.stringify({
            folder: this.currentFolder,
            folderName: ""
        }))
        api.post("browse/upload", data, undefined, true)
            .catch(console.log) // TODO человеческая обрабока ошибки
            .finally(() => {
              this.loading = false
              this.fetchFolder()
              this.closePopup()
            })
      }
    },
    changeFolder(file: FileDetails) {
      if(file.extension === 'directory') {
        if (file.name !== '..') {
          this.currentFolder = this.currentFolder ? (this.currentFolder + '/' + file.name) : file.name
        } else {
          const folderArr = this.currentFolder.split('/')
          folderArr.splice(-1)
          this.currentFolder = folderArr.join('/')
        }
        this.fetchFolder()
      }
    },
    deleteFile(file: FileDetails) {
      const body: string = JSON.stringify({name: file.name, path: this.currentFolder})
      api.post("browse/delete", body, new Headers({'Content-Type': 'application/json'}), true)
          .then(() => this.fetchFolder())
    },
    fetchFolder() {
      this.loading = true
      if (this.currentFolder) {
        api.post("browse/folder", JSON.stringify({folder: this.currentFolder}), new Headers({'Content-Type': 'application/json'}), true)
            .then(response => response.json())
            .then(body => this.filesArr = [{name: "..", extension: "directory"}].concat(body))
            .finally(() => this.loading = false)
      } else {
        api.get("browse/initial", undefined, true)
            .then(response => response.json())
            .then(body => this.filesArr = body)
            .finally(() => this.loading = false)
      }
    },
    closePopup() {
      this.popupShow = false
    },
    showCreatePopup(inputType: string) {
      switch(inputType) {
        case 'file':
          this.popupType = PopupType.UPLOAD_FILE
          break
        case 'folder':
          this.popupType = PopupType.CREATE_FOLDER
          break
      }
      this.popupShow = true
    },
    newDirectoryNameChanged() {
      if (this.popupShow) {
        if (this.newDirectoryName && !this.validateDirectoryName(this.newDirectoryName)) {
          this.newDirectoryNameValid = false
          return
        }
      }
    },
    validateDirectoryName(name: string): boolean {
      const illegalChars: Array<string> = ['/', '\'', '\\']
      return !name.split('').some(char => illegalChars.includes(char))
    },
    createDirectory() {
      this.loading = true
      const headers: Headers = new Headers({'Content-Type': 'application/json'})
      api.post('browse/newFolder', JSON.stringify({folder: this.currentFolder, folderName: this.newDirectoryName}), headers, true)
          .then(() => this.fetchFolder())
          .finally(() => {
            this.loading = false
            this.popupShow = false
            this.newDirectoryName = ""
            this.newDirectoryNameValid = true
          })
    }
  }
})
</script>

<style scoped>

.centerDiv {
  position: absolute;
  width: 50%;
  top:40%;
  left: 24%;
  background-color: rgba(39, 40, 44, 0.87);
}

table {
  font-family: -apple-system,BlinkMacSystemFont,"Segoe UI",Helvetica,Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji";
  color: lightblue;
  border-collapse: collapse;
  width: 100%;
}

td, th {
  border: 1px solid #dddddd;
  border-radius: 10px;
  text-align: left;
  padding: 8px;
}

.borderlessTh {
  border: none !important;
}

.popup {
  z-index: 101;
  position: fixed;
  width: 40%;
  height: auto;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background: inherit;
  border: 1px solid #dddddd;
  border-radius: 10px;
  background-color: #202425;
}

.text-areas {
  width: 51%;
  border: none;
  border-bottom: 2px solid #4f7cbd;
  background-color: rgba(70, 78, 91, 0.87);
  font-size: 15px;
  color: lightblue;
  font-family: -apple-system,BlinkMacSystemFont,"Segoe UI",Helvetica,Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji";
  position: relative;
  box-shadow: 1px 4px 10px 1px rgb(47 61 64) inset;
}

.popup_inner_content {
  padding: 2%;
  width: 100%;
}

.create-button {
  display: inline-block;
  border-radius: 20px;
  background-color: #4f7cbd;
  border: none;
  color: #FFFFFF;
  text-align: center;
  font-size: 16px;
  padding: 4px;
  width: 15%;
  cursor: pointer;
  margin: 5px;
}

.upload-button {
  border-radius: 20px;
  background-color: #4f7cbd;
  border: none;
  color: #FFFFFF;
  cursor: pointer;
  font-size: 15px;
}

.download-button-position {
  float: right;
  margin-right: 2%;
}

.buttons-div-position {
  float: right;
  margin-right: 2%;
}

.th-bottom-border {
  border: none;
  border-bottom: 2px solid #4f7cbd;
}

.popup-loading {
  filter: blur(2px);
}

.v-enter-active {
  transition: all 0.3s ease-out;
}

.v-leave-active {
  transition: all 0.3s ease-in;
}

.v-enter-from {
  opacity: 0;
  transform: translateX(50px) translateY(0);
}

.v-leave-to {
  opacity: 0;
  transform: translateX(-50px) translateY(0);
}
</style>