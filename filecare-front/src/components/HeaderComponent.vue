<template>
  <div class="header-component">
    <div>
      <span>
        {{ `user: ${username}` }}
      </span>
    </div>
    <div>
      <button @click="logout" class="logout-button">
        <span>
          logout
        </span>
      </button>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import api from "@/api/index";
import {UserDetails} from "@/interfaces";

export default defineComponent({
  name: "HeaderComponent",
  data() {
    return {
      username: ""
    }
  },
  mounted() {
    const userDetails: UserDetails | null = api.obtainUserDetails();
    if (userDetails && userDetails.username) {
      this.username = userDetails.username
    } else {
      this.$router.push({name: 'LoginView'})
    }
  },
  methods: {
    logout() {
      api.removeUserData()
      this.$router.push({name: 'LoginView'})
    }
  }
})
</script>

<style scoped>

.header-component {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  line-height: 1.5;
  background-color: rgba(70, 78, 91, 0.87);
  padding: 5px;
  border: none;
  border-bottom: 2px solid #4f7cbd;
  box-shadow: 1px 4px 10px 1px rgb(47 61 64) inset;
}

.header-component > div {
  margin: 0 25px 0 15px;
}

.logout-button {
  display: inline-block;
  text-align: center;
  width: 10%;
  transition: all 0.5s;
  cursor: pointer;
  opacity: 0.6;
  background: none;
  color: inherit;
  border: none;
  padding: 0;
  font: inherit;
  outline: inherit;
}

.logout-button:hover {
  opacity: 1;
}



</style>