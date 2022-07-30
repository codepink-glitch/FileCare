import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import BrowseView from '../views/BrowseView.vue'
import api from "@/api";
import {UserDetails} from "@/interfaces";
const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'LoginView',
    component: LoginView
  },
  {
    path: '/browse',
    name: 'BrowseView',
    component: BrowseView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to) => {
  const userDetails: UserDetails | null = api.obtainUserDetails();
  if ((userDetails === null || !userDetails.username || !userDetails.password) && to.name !== 'LoginView') {
    return {name: 'LoginView'}
  }
})

export default router
