import Vue from 'vue'
import Router from 'vue-router'
// import HelloWorld from '@/components/HelloWorld'
import MainVue from '@/components/MainVue'
import CodeVue from '@/components/CodeVue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'MainVue',
      component: MainVue
    },
    {
      path: '/code',
      name: 'CodeVue',
      component: CodeVue
    }
  ]
})
