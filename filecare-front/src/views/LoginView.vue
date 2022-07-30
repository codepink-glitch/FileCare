<template>
  <div>
    <div v-show="loading" style="z-index: 100; position:absolute; left:50%; top:43%; transform: translate(-50%, 0);"><div class="lds-heart"><div></div></div></div>
      <div :class="loading ? 'blur centerDiv rounded-border' : shaking ? 'shake centerDiv rounded-border' : 'centerDiv rounded-border'">
      <div class="grid-div inner-padding">
        <span>login: </span>
        <input v-model="authenticationRequest.username"
               @change="usernameChanged"
               :style="usernameValid ? '' : 'border-color: red;'"
               class="text-areas"
               type="text"/>
      </div>
      <div class="margin-top grid-div inner-padding">
        <span>password: </span>
        <input v-model="authenticationRequest.password"
              class="text-areas"
              type="password">
      </div>
      <div class="margin-top grid-div">
        <button @click="handleLoginButton" class="login-button" style="vertical-align: middle; width: 100%;">
          <span>{{ this.mode === 'login' ? 'login' : 'sign up' }}</span>
        </button>
        <button v-show="mode === 'login'" @click="changeMode" class="usual-button" style="vertical-align: middle; width: 50%; margin-left:45%">
          <span>register</span>
        </button>
      </div>
      <div v-show="showMessage" class="margin-top" style="text-align: center;">
        <span>{{ message }}</span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import api from "@/api";

export default defineComponent({
  name: "LoginView",
  data() {
    return {
      loading: false,
      mode: "login",
      showMessage: false,
      message: "",
      authenticationRequest: {
        username: "",
        password: ""
      },
      usernameValid: true,
      shaking: false,
    }
  },
  methods: {
    usernameChanged() {
      if (this.mode === "register") {
        if (!this.validateUsername(this.authenticationRequest.username)) {
          this.usernameValid = false
          return
        }

        api.get(`registration?username=${this.authenticationRequest.username}`, undefined, false)
            .then(response => response.json())
            .then(body => this.usernameValid = body)
      }
    },
    changeMode() {
      this.mode = this.mode === "login" ? "register" : "login"
      if (this.mode !== "register") {
        this.usernameValid = true
      }
    },
    handleLoginButton() {
      if (this.mode === "register") {
        this.register()
      } else if (this.mode === "login") {
        this.login();
      }
    },
    validateUsername(username: string): boolean {
      const illegalChars: Array<string> = ['<', '>', '?', '!', '=', '[', ']', "'", '"', ';', ':', ',']
      return !username.split('').some(char => illegalChars.includes(char))
    },
    async register() {
      if (!this.usernameValid || !this.authenticationRequest.username || !this.authenticationRequest.password) {
        this.editAndShowMessage("Please enter correct username/password")
        this.setShakingWithTimeout(500)
        return
      }
      const headers: Headers = new Headers({'Content-Type': 'application/json'})
      const body: string = JSON.stringify(this.authenticationRequest)
      this.loading = true
      await api.post('registration', body, headers, false)
          .then(response => {
            if (response.status !== 200) {
              this.loading = false
              this.editAndShowMessage("Incorrect login/password")
              return
            }
            this.loading = false
            this.editAndShowMessage("account registration successful!")
            this.resetFields()
            this.changeMode()
          }).catch(() => this.loading = false)
    },
    editAndShowMessage(message: string) {
      this.message = message
      this.showMessage = true
      setTimeout(() => this.showMessage = false, 3000)
    },
    setShakingWithTimeout(timeout: number) {
      this.shaking = true
      setTimeout(() => this.shaking = false, timeout)
    },
    resetFields() {
      this.authenticationRequest.username = ""
      this.authenticationRequest.password = ""
    },
    async login() {
      if (!this.authenticationRequest.username || !this.authenticationRequest.password) {
        this.editAndShowMessage("Please specify login/password")
        this.setShakingWithTimeout(500)
        return
      }
      const headers: Headers = new Headers({'Content-Type': 'application/json'})
      const body: string = JSON.stringify(this.authenticationRequest)
      this.loading = true
      await api.post('authentication', body, headers, false)
          .then(response => {
            if (response.status !== 200) {
              this.loading = false
              this.editAndShowMessage("Incorrect login/password")
              this.setShakingWithTimeout(500)
              this.resetFields()
              return
            }
            this.loading = false
            return response.json()
          }).then(responseBody => {
            localStorage.setItem('filecare-userdetails', JSON.stringify(this.authenticationRequest))
            localStorage.setItem('filecare-token', responseBody.token)
            this.resetFields()
            this.loading = false
            // TODO тут переход на следующую страницу
            this.$router.push({name: 'BrowseView'})
          }).catch(() => this.loading = false)
    }
  }
})
</script>

<style scoped>

.centerDiv {
  position: absolute;
  width: 33%;
  top: 40%;
  left: 31%;
  background-color: rgba(39, 40, 44, 0.87);
}

.grid-div {
  display: grid;
  margin: 0 auto;
  grid-template-columns: 1fr 2fr;
}

.text-areas {
  width: 100%;
  border: none;
  border-bottom: 2px solid #4f7cbd;
  background-color: rgba(70, 78, 91, 0.87);
  font-size: 15px;
  color: lightblue;
  font-family: -apple-system,BlinkMacSystemFont,"Segoe UI",Helvetica,Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji";
  position: relative;
  box-shadow: 1px 4px 10px 1px rgb(47 61 64) inset;
}

.margin-top {
  margin-top: 3%;
}

.inner-padding {
  padding-left: 5%;
  padding-right: 5%;
}

.login-button {
  display: inline-block;
  border-radius: 20px;
  background-color: #4f7cbd;
  border: none;
  color: #FFFFFF;
  text-align: center;
  font-size: 16px;
  padding: 4px;
  width: 10%;
  transition: all 0.5s;
  cursor: pointer;
  margin: 5px;
}

.usual-button {
  display: inline-block;
  border-radius: 20px;
  background-color: #4f7cbd;
  border: none;
  color: #FFFFFF;
  text-align: center;
  font-size: 16px;
  padding: 4px;
  width: 10%;
  transition: all 0.5s;
  cursor: pointer;
  margin: 5px;
  opacity: 0.6;
}

.usual-button:hover {
  opacity: 1;
}

.login-button span {
  cursor: pointer;
  display: inline-block;
  position: relative;
  transition: 0.5s;
}

.login-button span:after {
  content: '\00bb';
  position: absolute;
  opacity: 0;
  top: 0;
  right: -20px;
  transition: 0.5s;
}

.login-button:hover span {
  padding-right: 25px;
}

.login-button:hover span:after {
  opacity: 1;
  right: 0;
}

/*shake animation */
.shake {
  animation: shake 0.82s cubic-bezier(0.36, 0.07, 0.19, 0.97) both;
  transform: translate3d(0, 0, 0);
}

@keyframes shake {
  10%,
  90% {
    transform: translate3d(-1px, 0, 0);
  }

  20%,
  80% {
    transform: translate3d(2px, 0, 0);
  }

  30%,
  50%,
  70% {
    transform: translate3d(-4px, 0, 0);
  }

  40%,
  60% {
    transform: translate3d(4px, 0, 0);
  }
}

</style>