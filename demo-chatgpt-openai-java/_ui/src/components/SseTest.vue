<template>
  <div>
    <div>
      <span style="display: block;color: #acacac">请输入clientId然后点击按钮连接sse</span>

      <input v-model="clientId" style="width: 300px;margin: 0 15px 0 15px"/>
      <button @click="createSSE">连接sse</button>
      <div style="margin: 20px 15px">
        <input v-model="msg" style="width: 300px;margin: 0 15px 0 15px"/>
        <button @click="sendMsg">提交</button>
      </div>
    </div>
    <div class="content">
      <div v-for="item in eventData" v-bind:key="item.id" :class="item.styleClass">
        <span style="display: block">{{item.content}}</span>
      </div>
    </div>
    <div>

    </div>

  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import {EventSourcePolyfill} from 'event-source-polyfill'
  import axios from "axios";

  const url = 'http://localhost:8999/api'

  export default {
    name: "SseTest",
    data() {
      return {
        clientId: null,
        sseEvent: null,
        msg: '',
        eventData: [
        ]
      }
    },
    created() {

    },
    methods: {

      createSSE() {
        const that = this;
        if (window.EventSource) {
          this.sseEvent = new EventSourcePolyfill(`${url}/sse/stream/${this.clientId}`, {
              heartbeatTimeout: 60 * 60 * 1000,
              headers: {
                // 这里可以带token
                'Authorization': 'xxx',
              },
            });
          this.sseEvent.onopen = function (event) {
            console.log("连接成功", event);
          };

          this.sseEvent.onmessage = function (event) {
            let popData = that.eventData[that.eventData.length - 1];
            popData.content += event.data;
            console.log("接收信息", event);
          };

          this.sseEvent.onerror = function (error) {
            console.log("错误", error);
          };
        } else {
          console.log("不支持sse")
        }
      },
      beforeDestroy() {
        if (this.sseEvent) {
          // 关闭连接
          this.sseEvent.close();
          this.sseEvent = null
          axios.get(`${url}/sse/close`,{
            params: {
              clientId: this.clientId
            }
          }).then(res => console.log(res));
        }
      },
      sendMsg(){

        let userData = {
          styleClass: 'content-user',
          content: JSON.parse(JSON.stringify(this.msg))
        }
        this.eventData.push(userData)

        let assistantData = {
          styleClass: 'content-assistant',
          content: ""
        }
        this.eventData.push(assistantData)
        axios.get(`${url}/sse/send`,{
          params: {
            clientId: this.clientId,content: this.msg
          }
        });
      }
    },
    destroyed() {
      axios.get(`${url}/sse/close`,{
        params: {
          clientId: this.clientId
        }
      }).then(res => console.log(res));
    }
  }
</script>

<style scoped>
  .content{
    width: 80%;
    height: auto;
    margin: 0 auto;
  }

  .content-user{
    width: 100%;
    height: auto;
    padding: 25px;
    border: 1px #0071ff solid;
  }
  .content-user span{
    float: left;
  }
  .content-assistant{
    width: 100%;
    height: auto;
    padding: 25px;
    border: 1px #00ff22 solid;
  }
  .content-assistant span{
    float: right;
  }
</style>
