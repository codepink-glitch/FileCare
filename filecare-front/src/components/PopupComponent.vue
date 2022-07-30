<template>
  <div class="popup">
    <div class="popup_header">
      <span>
        <i @click="closePopup" class="popup_close">
          <span>close</span>
        </i>
      </span>
    </div>
    <div class="popup_content">
      <slot></slot>
    </div>
    <div class="popup_footer"></div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "PopupComponent",
  methods: {
    closePopup() {
      this.$emit('closePopup')
    }
  }
})
</script>

<style scoped>

.popup_header {
  width: 100%;
  display: flex;
  justify-content: flex-end;
}

.popup_footer {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup_close {
  margin: 0.5em;
  transition: all 0.5s;
  cursor: pointer;
  opacity: 0.6;
}

.popup_close:hover {
  opacity: 1;
}

.popup_content {
  display: flex;
  justify-content: center;
  align-items: center;
}

</style>