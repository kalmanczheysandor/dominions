const { defineConfig } = require('@vue/cli-service')
const path = require('path');
module.exports = defineConfig({
    devServer: {
        port: 8080
    },
    transpileDependencies: true,
    configureWebpack: {
        resolve: {
            alias: {
                '@': path.resolve(__dirname, 'src'),
                'vue$': 'vue/dist/vue.esm-bundler.js'
            },
        },
    },
})