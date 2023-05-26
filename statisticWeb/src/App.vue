<template>
  <div id="app">
    <router-link to="/"><button class="my_button"><span>主视图</span></button></router-link>
    <router-link to="/code"><button class="my_button"><span>其它视图</span></button></router-link>
    <router-view />
  </div>
</template>

<script>

export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.my_button {
  display: inline-block;
  border-radius: 4px;
  background-color: rgb(51, 86, 174);
  border: none;
  color: #FFFFFF;
  text-align: center;
  font-size: 28px;
  padding: 20px;
  width: 200px;
  transition: all 0.5s;
  cursor: pointer;
  margin: 5px;
}

.my_button span {
  cursor: pointer;
  display: inline-block;
  position: relative;
  transition: 0.5s;
}

.my_button span:after {
  content: '\00bb';
  /* CSS Entities. 如果用的是 HTML Entities, 请改成 &#8594;*/
  position: absolute;
  opacity: 0;
  top: 0;
  right: -20px;
  transition: 0.5s;
}

.my_button:hover span {
  padding-right: 25px;
}

.my_button:hover span:after {
  opacity: 1;
  right: 0;
}
</style>
