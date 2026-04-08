const {defineConfig} = require('@vue/cli-service')
const path = require('path');
module.exports = defineConfig({
    // devServer: {
    //   host: '0.0.0.0',       // minden hálózati interfészen elérhető
    //   port: 8080,            // a port, amit használsz
    //   allowedHosts: [
    //     'dominions.hu',
    //     // 'admin.dominions.hu'
    //   ],
    //   // opcionális: hot reload fix
    //   headers: {
    //     'Access-Control-Allow-Origin': '*'
    //   }
    // },

    devServer: {
        host: '0.0.0.0',
        port: 8080,
        allowedHosts: ['dominions.hu', 'admin.dominions.hu'],
        headers: {
            'Access-Control-Allow-Origin': '*'
        },
        proxy: {
            '/': {
                target: 'http://localhost:8080', // Spring backend
                changeOrigin: true,
                secure: false,
                cookieDomainRewrite: 'dominions.hu'
            }
        }
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
