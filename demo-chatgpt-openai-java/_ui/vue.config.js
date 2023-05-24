
module.exports = {
  devServer: {
    host: '0.0.0.0',
    port: 8999,
    open: true,
    // 不设置这个，无法接收到onmessage的信息
    compress: false,
    proxy: {
      // detail: https://cli.vuejs.org/config/#devserver-proxy
      '/api': {
        target: `http://localhost:8888`,
        changeOrigin: true,
        pathRewrite: {
          ['^' + '/api']: ''
        }
      }
    }
  },
}
