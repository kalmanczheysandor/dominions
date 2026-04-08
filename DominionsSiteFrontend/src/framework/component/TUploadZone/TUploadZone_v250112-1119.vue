<template>
    <div class="TUploadZone" ref='TUploadZone' :style="styleUploadBox" @drop="eventOnDrop" @dragenter="eventOnDragEnter" @dragover="eventOnDragOver" >
        <div class="Actions">
            <v-icon class="Icon" v-show="isTrashIconVisible" @click.stop="eventTrashIconClicked">
                mdi-delete-outline
            </v-icon>
        </div>
    </div>
</template>

<script>


    export default {
        props: {
            config: {
                type: Object,
                required: false,
                default: () => ({
                    width: 150,
                    height: 150
                })
            },
            target: {
                type: String,
                required: false,
                default: null
            },
        },
        data() {
            return {
                uploadList: [],
                maxThread: 1,
                threadCount: 0,
                currentItemKey: 0
            };
        },
        computed: {
            styleUploadBox() {
                return {
                    width: this.config.width + 'px',
                    height: this.config.height + 'px',
                };
            },
            isTrashIconVisible() {
                return this.uploadList.length > 0;
            }
        },
        methods: {

            eventTrashIconClicked(event) {
                event.stopPropagation();
                event.preventDefault();

                this.clearDropArea();
                this.uploadList = [];
            },
            eventItemClicked(itemKey, rootNode) {
                this.removeItemFromUploadListByKey(itemKey);
                rootNode.parentNode.removeChild(rootNode);
            },
            eventOnDragOver(event) {
                event.stopPropagation();
                event.preventDefault();
            },
            eventOnDragEnter(event) {
                event.stopPropagation();
                event.preventDefault();
            },
            eventOnDrop(event) {
                event.stopPropagation();
                event.preventDefault();

                const filesArr = event.dataTransfer.files;
                for(let file of filesArr) {
                    this.registrateImage(file);
                }
                this.uploader();
            },
            uploader() {
                if((this.uploadList.length > 0) && (this.maxThread > this.threadCount)) {
                    let anItem = this.uploadList[0];
                    this.fileUploader(anItem);
                }
            },
            fileUploader(queueItem) {
                const self = this;
                this.threadCount++;

                const theHttp = new XMLHttpRequest();

                theHttp.addEventListener("load", function() {
                    if(this.status === 200 || this.status === 201) {
                        try {
                            //const response = JSON.parse(this.responseText);
                            self.threadCount--;

                        } catch(e) {
                            console.error(e);
                        }
                    } else {
                        console.error("Error, status:", this.status, "statusText:", this.statusText);
                    }

                    if(self.uploadList.length > 0) {
                        self.uploader();
                    } else {
                        self.onEndOfQueue();
                    }

                });

                theHttp.addEventListener("error", function() {
                    self.onItemFinishedWithError(queueItem);
                    self.removeItemFromUploadListByKey(queueItem.key);

                });

                theHttp.upload.addEventListener("progress", function(e) {
                    if(e.lengthComputable) {
                        let percentage = Math.round((e.loaded / e.total) * 100);
                        queueItem.progressScale.innerHTML = `${percentage}%`;
                    }
                }, false);

                theHttp.upload.addEventListener("load", function(e) {
                    self.onItemFinishedWithSuccess(queueItem);
                    self.removeItemFromUploadListByKey(queueItem.key);
                }, false);

                // Initialise form
                const theForm = new FormData();
                theForm.append('files', queueItem.imageFile);

                // Send request
                theHttp.withCredentials = true;
                theHttp.open("POST", this.target, true);
                theHttp.send(theForm);
            },

            registrateImage(imageFile) {
                const itemKey = this.generateNextItemKey();
                const nodeData = this.generateNewImageNode(imageFile, itemKey);

                this.$refs.TUploadZone.appendChild(nodeData.frameNode);
                let obj = {
                    key: itemKey,
                    imageFile: imageFile,
                    imageElement: nodeData.imgNode,
                    progressScale: nodeData.progressScaleNode,
                    rootNode: nodeData.frameNode
                };

                this.uploadList.push(obj);
            },
            generateNewImageNode(file, itemKey) {
                let rootNode = document.createElement("div");
                rootNode.classList.add("ImageFrame");

                let progressScale = document.createElement("div");
                progressScale.classList.add("ProgressScale");


                let progressBar = document.createElement("div");
                progressBar.classList.add("ProgressBar");
                progressBar.appendChild(progressScale);

                let img = document.createElement("img");


                if(file.type.match(/image.*/)) {
                    img.src = window.URL.createObjectURL(file);
                    img.height = 80;
                    img.classList.add("TheImage");
                    img.onload = function() {
                        window.URL.revokeObjectURL(this.src);
                    }
                } else if(file.type.match(/video.*/)) {
                    img.src = '/image/movie-icon.jpg';
                    img.height = 80;
                    img.classList.add("TheImage");
                } else {
                    img.src = '/image/file-icon.jpg';
                    img.height = 80;
                    img.classList.add("TheImage");
                }

                const self = this;
                rootNode.appendChild(img);
                rootNode.appendChild(progressBar);
                rootNode.addEventListener("click", () => {
                    self.eventItemClicked(itemKey, rootNode);
                });
                return {
                    frameNode: rootNode,
                    imgNode: img,
                    progressScaleNode: progressScale
                }
            },

            generateNextItemKey() {
                this.currentItemKey++;
                return this.currentItemKey;
            },
            removeItemFromUploadListByKey(key) {
                let itemToFind = this.uploadList.find(item => item.key === key);
                if(itemToFind) {
                    this.uploadList = this.uploadList.filter(item => item !== itemToFind);
                }
            },

            clearDropArea() {
                alert("cleared");
                let nodesList = this.$refs.TUploadZone.querySelectorAll(".ImageFrame");
                if(nodesList != null) {
                    nodesList.forEach(function(itemNode) {
                        itemNode.parentNode.removeChild(itemNode);
                    });

                }
            },

            onEndOfQueue() {
               // this.clearDropArea();
                this.uploadList = [];
                this.$emit('whenUploadFinished');
            },
            onItemFinishedWithSuccess(queueItem) {
                queueItem.rootNode.classList.add("FinishedItem");
                queueItem.rootNode.scrollIntoView({
                    behavior: 'smooth', // Smooth scrolling
                    block: 'center',    // Align the element to the center of the viewport
                });
            },
            onItemFinishedWithError(queueItem) {
                queueItem.rootNode.classList.add("FailedItem");
                queueItem.rootNode.scrollIntoView({
                    behavior: 'smooth', // Smooth scrolling
                    block: 'center',    // Align the element to the center of the viewport
                });
            }
        }

    };
</script>

<style>
    .FinishedItem {
    //border:1px green solid !important;
    }

    .FinishedItem img {
        opacity: 0.2;
    }

    .FailedItem {
        border: 2px #cc0000 solid !important;
    }

    .FailedItem img {
        opacity: 0.2;
    }

    .TUploadZone {
position:relative;
        display: flex;
        flex-wrap: wrap;
        flex-direction: row;
        align-items: flex-start;
        gap: 10px;


        margin: 5px;

        box-sizing: border-box;


        font-family: Arial;

        font-size: 14px;
        color: #657786;


        border: 1px #c1c1c1 dashed;
        background-color: #fafafa;

        background-repeat: no-repeat;
        background-position: center;
        overflow-y: scroll;
        height:200px;

    }


    .TUploadZone::-webkit-scrollbar {
        width: 10px;
        height: 10px;
        background-color: #f2f2f2;
    }


    .TUploadZone::-webkit-scrollbar-track {
        width: 10px;
        height: 10px;
        background-color: #f2f2f2;
    }

    .TUploadZone::-webkit-scrollbar-thumb {
        background-color: #e6e6e6;
    }

    .TUploadZone::-webkit-scrollbar-thumb:hover {
        background-color: #cccccc;
    }

    .TUploadZone > .Actions {
        position: relative;
        top: 0px;
        right:0px;
        width:100%;
        height: 30px;
        z-index: 2;
        border:1px red solid;
    }

    .TUploadZone > .Actions > .Icon {
    //text-shadow: 0px 0px 2px black; text-shadow: #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px; color: white; font-size: 30px; opacity: 0.2;
    }

    .TUploadZone > .Actions > .Icon:hover {
        text-shadow: 0px 0px 2px black;
        color: white;
        font-size: 30px;
        cursor: pointer;
        opacity: 1;
    }


    .TUploadZone .ImageFrame {
        position: relative;
        flex: 1 1 calc(25% - 10px);
        max-width: calc(25% - 10px);
        background-color: transparent;
        border: 1px grey solid;
        padding: 3px;
        height: auto;
        max-height: calc(100% * 4 / 3);
    }

    .TUploadZone .ImageFrame .TheImage {
        margin: 0px;
        padding: 0px;




        font-family: Arial;
        font-size: 15px;
        color: #cc0000;
        text-align: left;

        border: none;

        background-color: transparent;
        box-sizing: border-box;


        object-fit: cover;
        height:100%;
        width:100%;


    }

    .TUploadZone .ImageFrame .ProgressBar {
        position: absolute;
        top: 50%;
        left: 50%;

        margin: 0;
        padding: 0;
        height: 5px;
        border: none;
        background-color: transparent;
        box-sizing: border-box;


        text-shadow: #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px, #000 0px 0px 1px;
        color: #ffffff;
        transform: translate(-50%, -50%); /* Az elem középre igazítása mindkét irányban */
    }


    .TUploadZone .ImageFrame .ProgressBar .ProgressScale {
        margin: 0px;
        padding: 0px;
        height: 5px;

        border: none;
        box-sizing: border-box;
    }


</style>

