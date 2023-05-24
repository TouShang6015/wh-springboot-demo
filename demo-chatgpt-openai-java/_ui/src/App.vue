<template>
  <div id="app">
    <sse-test></sse-test>
  </div>
</template>

<script>
import SseTest from './components/SseTest'

export default {
  name: 'App',
  components: {
    SseTest
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
