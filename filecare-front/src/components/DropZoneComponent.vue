<template>
  <div
      @dragenter.prevent="toggleActive"
      @dragleave.prevent="toggleActive"
      @dragover.prevent
      @drop.prevent="toggleActive"
      :class="{ 'active-dropzone': active }"
      class="dropzone">
    <span>Drag or drop file</span>
    <span>OR</span>
    <label for="dropzoneFile">Select file</label>
    <input type="file" id="dropzoneFile" class="dropzone-file" />
  </div>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";

export default defineComponent({
  name: "DropZoneComponent",
  setup() {
    const active = ref(false)

    const toggleActive = () => {
      active.value = !active.value
    }

    return {active, toggleActive}
  }
})
</script>

<style scoped>

.dropzone {
  /*width: 500px;*/
  /*height: 200px;*/
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  row-gap: 16px;
  border: 2px dashed lightblue;
  background-color: #202425;
  transition: 0.3s ease all;
}

.dropzone > label {
  border-radius: 20px;
  background-color: #4f7cbd;
  border: none;
  color: #FFFFFF;
  cursor: pointer;
  margin: 5px;
  padding: 5px 15px;
  transition: 0.3s ease all;
}

.dropzone > input {
  display: none;
}

.active-dropzone {
  color: #fff;
  border-color: #fff;
  background-color: rgba(70, 78, 91, 0.87);
}

.active-dropzone > label {
  border-radius: 20px;
  background-color: #4f7cbd;
  border: none;
  color: #FFFFFF;
  cursor: pointer;
  margin: 5px;
  padding: 5px 15px;
}

</style>