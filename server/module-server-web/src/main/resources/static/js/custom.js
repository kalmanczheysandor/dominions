///////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// TSummernoteFactory   /////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////


/**
 * @Url https://github.com/DiemenDesign/summernote-cleaner
 */
TSummernoteFactory = new function () {
    let self = this;
    this.defaultConfigurations = {
        height: 300,
        stripTags: true,
        toolbar: [
            ['style', ['style']],
            ['style', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
            ['color', ['forecolor']],
            ['para', ['ul', 'ol', 'height']],
            ['para', ['paragraph']],
            ['misc', ['undo', 'redo', 'codeview', 'fullscreen']],
        ],
        styleTags: ['p', 'h1', 'h2', 'h3', 'h4', 'h5'],
        /*
        cleaner: {
            action: 'both',
            newline: '<br>',
            icon: '<i class="note-icon">[Your Button]</i>',
            keepHtml: true,
            keepOnlyTags: ['<p>', '<br>', '<ul>', '<li>', '<b>', '<strong>', '<span>', '<font>', '<i>', '<a>', '<h1>', '<h2>', '<h3>', '<h4>', '<h5>', '<h6>', '<h7>', '<h8>', '<h9>'],
            keepClasses: false,
            badTags: ['<style>', '<script>', '<applet>', '<embed>', '<noframes>', '<noscript>', '<html>'],
            badAttributes: ['start'],
            limitChars: false,
            limitDisplay: 'both',
            limitStop: false
        }*/
        callbacks: {
            onPaste: function (e) {
                var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
                e.preventDefault();
                document.execCommand('insertText', false, bufferText);
            }
        }
    };
    this.autoAttach = function (configurations = null) {
        if (configurations === null) {
            configurations = self.defaultConfigurations;
        }
        $("[ffw-type='SummernoteEditor']").summernote(configurations);
    };
};
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// GOOGLE RECAPTCHA   ///////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

function getGoogleReCaptchaWidgetIdByElementId(elementId) {
    recaptchaBoxes = document.querySelectorAll('.g-recaptcha');
    targetBox = document.querySelector(`#${elementId}`);
    for (let i = 0; i < recaptchaBoxes.length; i++) {
        if (recaptchaBoxes[i].id === targetBox.id) {
            return i;
        }
    }
}



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////  ARRAYINPUT  ///////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class TArrayInputComponent {
    static componentType = "ArrayInput";
    static #instances = [];

    static #injectRecordValues(rowNode, fieldInputValueList) {
        fieldInputValueList.forEach((field) => {

            // Checkings
            if (field.name == undefined) {
                throw 'The "name" attribute is not defined!'
            }
            if (field.value == undefined) {
                throw 'The "value" attribute is not defined!'
            }

            // Find input field
            let inputNode = rowNode.querySelector('[name="' + field.name + '"]');
            if (inputNode == null || inputNode == undefined) {
                throw 'The targeted field with name="' + field.name + '" is not found!';
            }

            // Inject value
            TArrayInputComponent.#injectFieldValue(inputNode, field.name, field.value)

        });
        return rowNode;
    }

    static #injectFieldValue(inputNode, fieldName, fieldValue, fieldType = null, fieldData = null) {

        if (fieldType == null) {
            inputNode.setAttribute('value', fieldValue)
        } else if (fieldType == "text") {
            inputNode.setAttribute('value', fieldValue)
        } else {
            throw 'Unexpected case is found!';
        }
    }

    static detect() {
        // Find  nodes
        let notInitialisedComponentList = document.body.querySelectorAll('[ffw-type="' + TArrayInputComponent.componentType + '"]:not([ffw-isinitialised="true"])');
        if (notInitialisedComponentList === null || notInitialisedComponentList === undefined) {
            throw 'The list is null or undefined!';
        }

        // Perform initialisation
        notInitialisedComponentList.forEach(function (item) {
            let instance = new TArrayInputComponent(item);
            item.ffwTArrayInputComponent = instance;
            TArrayInputComponent.#instances.push(instance);
        });
        return TArrayInputComponent.#instances;
    };

    static isInstanceExistByFFWId(ffwId) {
        let rootNode = document.body.querySelector('[ffw-type="' + TArrayInputComponent.componentType + '"][ffw-id="' + ffwId + '"]');
        if (rootNode == null) {
            return false;
        }
        if (rootNode.ffwTArrayInputComponent == undefined) {
            return false;
        }
        return true;
    }

    static getInstanceByFFWId(ffwId) {

        let rootNode = document.body.querySelector('[ffw-type="' + TArrayInputComponent.componentType + '"][ffw-id="' + ffwId + '"]');
        if (rootNode == null) {
            return null;
        }
        if (rootNode.ffwTArrayInputComponent == undefined) {
            return null;
        }
        return rootNode.ffwTArrayInputComponent;
    }

    addRowEventCallback() {

    }

    removeRowEventCallback() {

    }

    eventRemoveButtonOnClick(event) {
        try {
            event.preventDefault();

            // Initialisation: button
            let buttonNode = event.currentTarget;
            if (buttonNode == null) {
                throw 'The "RemoveButton" component node is null!';
            }
            if (buttonNode.getAttribute("ffw-alias") != 'RemoveButton') {
                throw 'No "RemoveButton" alias is found!';
            }

            // Initialisation: menu line
            let menuLineNode = buttonNode.parentElement.parentElement.parentElement;
            if (menuLineNode == null) {
                throw 'The "MenuLine" target node is null!';
            }
            if (menuLineNode.getAttribute("ffw-target") != TArrayInputComponent.componentType + '.MenuLine') {
                throw 'No "MenuLine" target is found!';
            }

            // Initialisation: component root node
            let componentRootNode = buttonNode.parentNode.parentNode.parentNode.parentNode;
            if (componentRootNode == null) {
                throw 'The component(root) node is null!';
            }
            if (componentRootNode.getAttribute("ffw-type") != TArrayInputComponent.componentType) {
                throw 'No component(root) is found!';
            }


            //Find and remove belonging input field row
            let rowNode = menuLineNode.previousElementSibling;
            if (rowNode == null) {
                throw "No belonging row-node is found!";
            }
            menuLineNode.style.backgroundColor = "green";
            rowNode.remove();

            // Remove belonging menu-line node
            menuLineNode.remove();

            this.removeRowEventCallback();


        } catch (exp) {
            console.error(exp)
        }
    };

    eventAddButtonOnClick(event) {

        try {
            event.preventDefault();

            // Initialisation: button
            let buttonNode = event.currentTarget;
            if (buttonNode == null) {
                throw 'The "AddButton" component node is null!';
            }
            if (buttonNode.getAttribute("ffw-alias") != 'AddButton') {
                throw 'No "AddButton" alias is found!';
            }

            // Initialisation: menu line
            let menuLineNode = buttonNode.parentElement.parentElement.parentElement;
            if (menuLineNode == null) {
                throw 'The "MenuLine" target node is null!';
            }

            if (menuLineNode.getAttribute("ffw-target") != TArrayInputComponent.componentType + '.MenuLine') {
                throw 'No "MenuLine" target is found!';
            }

            // Initialisation: component root node
            let componentRootNode = buttonNode.parentNode.parentNode.parentNode.parentNode;
            if (componentRootNode == null) {
                throw 'The component(root) node is null!';
            }
            if (componentRootNode.getAttribute("ffw-type") != TArrayInputComponent.componentType) {
                throw 'No component(root) is found!';
            }

            // Insert new row
            this.#insertNewRowNode(componentRootNode, menuLineNode);

            this.addRowEventCallback();

            // update initial menu
            //self.updateInitialMenu(componentRootNode);
        } catch (exp) {
            console.error(exp)
        }
    };

    #updateInitialMenu(componentRootNode) {
        // Initialisation: component root node
        if (componentRootNode === null || componentRootNode === undefined) {
            throw 'The component(root) node is undefined!';
        }
        if (componentRootNode.getAttribute("ffw-type") != TArrayInputComponent.componentType) {
            throw 'No component(root) is found!';
        }

        let menuNodesList = componentRootNode.querySelectorAll('[ffw-target="' + TArrayInputComponent.componentType + '.MenuLine"]');
        let menuNodesList_whenInitial = componentRootNode.querySelectorAll('[ffw-target="' + TArrayInputComponent.componentType + '.MenuLine"][ffw-isinitial="true"]');
        let menuNodesList_whenNotInitial = componentRootNode.querySelectorAll('[ffw-target="' + TArrayInputComponent.componentType + '.MenuLine"]:not([ffw-isinitial="true"])');
        let countInitials = menuNodesList_whenInitial.length;
        let countNotInitials = menuNodesList_whenNotInitial.length;

        if (countInitials == 1 && countNotInitials == 1) {//when:first add
            menuNodesList.forEach(function (item) {
                if (item.getAttribute('ffw-isinitial') == 'true') {
                    item.remove();
                }
            });

        } else if (countInitials == 0 && countNotInitials == 0) {//when:first add
            this.#insertMenuLine(componentRootNode, null, true);
        }
    }

    #insertMenuLine(componentRootNode, attachAfterNode = null, isInitial = true) {
        // Checking input parameters
        if (componentRootNode == null) {
            throw 'The "componentRootNode" must not be null!';
        }
        if (isInitial !== true && isInitial !== false) {
            throw 'Parameter "isInitial" must to be boolean!';
        }

        // Generate menu content
        let menuFragment = document.createDocumentFragment();

        let menuFragment_div = document.createElement('div');
        menuFragment_div.setAttribute('style', '');
        menuFragment_div.setAttribute('ffw-target', TArrayInputComponent.componentType + '.MenuLine');
        menuFragment_div.setAttribute('ffw-isinitial', 'false');
        if (isInitial === true) {
            menuFragment_div.setAttribute('ffw-isinitial', 'true');
        }
        menuFragment.appendChild(menuFragment_div);

        let menuFragment_rowmenu = document.createElement('rowmenu');
        menuFragment_div.appendChild(menuFragment_rowmenu);

        //Rowmenu> icon
        let menuFragment_rowmenu_icon = document.createElement('img');
        menuFragment_rowmenu_icon.setAttribute('style', 'border:0px red solid;margin:0px;height:25px;padding:0px;');
        menuFragment_rowmenu_icon.setAttribute('src', 'template/admin/default/images/rowmenu/menu-ellipsis-vertical.svg');
        menuFragment_rowmenu.appendChild(menuFragment_rowmenu_icon);

        //Rowmenu> dropdown
        let menuFragment_dropdown = document.createElement('dropdown');
        menuFragment_rowmenu.appendChild(menuFragment_dropdown);

        //Rowmenu> dropdown > newItem
        let menuFragment_rowmenu_dropdown_newItem = document.createElement('item');
        menuFragment_rowmenu_dropdown_newItem.setAttribute('ffw-alias', 'AddButton');
        menuFragment_rowmenu_dropdown_newItem.addEventListener('click', this.eventAddButtonOnClick.bind(this));
        menuFragment_dropdown.appendChild(menuFragment_rowmenu_dropdown_newItem);

        //Rowmenu> dropdown > newItem > icon
        let menuFragment_rowmenu_dropdown_newItem_icon = document.createElement('img');
        menuFragment_rowmenu_dropdown_newItem_icon.setAttribute('src', 'template/admin/default/images/rowmenu/pen.svg');
        menuFragment_rowmenu_dropdown_newItem.appendChild(menuFragment_rowmenu_dropdown_newItem_icon);

        //Rowmenu> dropdown > newItem > label
        let menuFragment_rowmenu_dropdown_newItem_label = document.createElement('label');
        menuFragment_rowmenu_dropdown_newItem_label.innerText = this.ffwAddButtonCaption;
        menuFragment_rowmenu_dropdown_newItem.appendChild(menuFragment_rowmenu_dropdown_newItem_label);

        if (isInitial === false) {
            //Rowmenu> dropdown > removeItem
            let menuFragment_rowmenu_dropdown_removeItem = document.createElement('item');
            menuFragment_rowmenu_dropdown_removeItem.setAttribute('ffw-alias', 'RemoveButton');
            menuFragment_rowmenu_dropdown_removeItem.addEventListener('click', this.eventRemoveButtonOnClick.bind(this));
            menuFragment_dropdown.appendChild(menuFragment_rowmenu_dropdown_removeItem);

            //Rowmenu> dropdown > removeItem > icon
            let menuFragment_rowmenu_dropdown_removeItem_icon = document.createElement('img');
            menuFragment_rowmenu_dropdown_removeItem_icon.setAttribute('src', 'template/admin/default/images/rowmenu/trash2.svg');
            menuFragment_rowmenu_dropdown_removeItem.appendChild(menuFragment_rowmenu_dropdown_removeItem_icon);

            //Rowmenu> dropdown > removeItem > label
            let menuFragment_rowmenu_dropdown_removeItem_label = document.createElement('label');
            menuFragment_rowmenu_dropdown_removeItem_label.innerText = this.ffwRemoveButtonCaption;
            menuFragment_rowmenu_dropdown_removeItem.appendChild(menuFragment_rowmenu_dropdown_removeItem_label);
        }

        // Attach after
        let n = null;
        if (attachAfterNode === null) {
            n = componentRootNode.appendChild(menuFragment_div, null);
            return menuFragment_div;
        } else if (attachAfterNode.nextElementSibling !== null && attachAfterNode.nextElementSibling !== undefined) {
            n = componentRootNode.insertBefore(menuFragment_div, attachAfterNode.nextElementSibling);
            return menuFragment_div;
        } else if (attachAfterNode.nextElementSibling === null || attachAfterNode.nextElementSibling === undefined) {// when it is the last node
            n = componentRootNode.appendChild(menuFragment_div);
            return menuFragment_div;
        } else {
            throw 'Undefined case is found!'
        }
    }

    #insertNewRowNode(componentRootNode, attachAfterNode = null, fieldValuesList = [], recordKey = null) {
        try {
            // Initialisation: Captions
            let defaultAddButtonCaption = 'Igen';
            let defaultRemoveButtonCaption = 'Nem';
            let ffwAddButtonCaption = componentRootNode.getAttribute("ffw-addbutton-caption");
            let ffwRemoveButtonCaption = componentRootNode.getAttribute("ffw-removebutton-caption");
            if (ffwAddButtonCaption == null) {
                ffwAddButtonCaption = defaultAddButtonCaption;
            }
            if (ffwRemoveButtonCaption == null) {
                ffwRemoveButtonCaption = defaultRemoveButtonCaption;
            }

            // Find template inside component and get fragment
            let templateNode = componentRootNode.querySelector('template');
            if (templateNode == null) {
                throw "Template is empty!";
            }
            let templateFragment = templateNode.content.cloneNode(true);

            // Determine root node in fragment
            let templateFragmentRootNode = templateFragment.firstElementChild;
            if (templateFragmentRootNode == null) {
                throw "No root node exists in fragment!";
            }

            // Add attribute to first node
            templateFragmentRootNode.setAttribute('ffw-target', TArrayInputComponent.componentType + '.Row');


            if (fieldValuesList.length > 0) {
                templateFragmentRootNode = TArrayInputComponent.#injectRecordValues(templateFragmentRootNode, fieldValuesList);
            }


            let ffwIncrement = componentRootNode.getAttribute("ffw-increment");
            ffwIncrement++;
            let keyToInject = '::' + ffwIncrement;
            if (recordKey != null) {
                keyToInject = recordKey;
            }

            let attributeNameReplaceList = templateFragmentRootNode.querySelectorAll('[name*="[]"]');
            attributeNameReplaceList.forEach(function (item) {
                let value = item.getAttribute('name');
                if (value.includes("[]")) {
                    value = value.replaceAll("[]", '[' + keyToInject + ']');
                    item.setAttribute('name', value);
                }
            });

            let attributeForReplaceList = templateFragmentRootNode.querySelectorAll('[for*="[]"]');
            attributeForReplaceList.forEach(function (item) {
                let value = item.getAttribute('for');
                if (value.includes("[]")) {
                    value = value.replaceAll("[]", '[' + keyToInject + ']');
                    item.setAttribute('for', value);
                }
            });
            componentRootNode.setAttribute("ffw-increment", ffwIncrement)


            // Attach after
            let newRowNode = null;
            if (attachAfterNode === null) { // No node to attache after
                newRowNode = componentRootNode.appendChild(templateFragmentRootNode);
            } else if (attachAfterNode.nextElementSibling !== null && attachAfterNode.nextElementSibling !== undefined) {   // when there is a next elemnt
                newRowNode = componentRootNode.insertBefore(templateFragmentRootNode, attachAfterNode.nextElementSibling);
            } else if (attachAfterNode.nextElementSibling === null || attachAfterNode.nextElementSibling === undefined) {   // when it is the last node
                newRowNode = componentRootNode.appendChild(templateFragmentRootNode);
            } else {
                throw 'Undefined case is found!'
            }


            // Add navigation line
            this.#insertMenuLine(componentRootNode, newRowNode, false);
        } catch (exp) {
            console.error(exp)
        }
    }

    #initialiseComponent(componentRootNode) {
        try {

            // Set variable
            let ffwAddButtonCaption = componentRootNode.getAttribute('ffw-addbutton-caption');
            if (ffwAddButtonCaption != null) {
                this.ffwAddButtonCaption = ffwAddButtonCaption;
            }

            // Set variable
            let ffwRemoveButtonCaption = componentRootNode.getAttribute('ffw-removebutton-caption');
            if (ffwRemoveButtonCaption != null) {
                this.ffwRemoveButtonCaption = ffwRemoveButtonCaption;
            }

            // Find template
            let templateNode = componentRootNode.querySelector('template');
            if (templateNode === null || templateNode === undefined) {
                throw "No template is found!!";
            }

            // Add initialised attribute
            componentRootNode.setAttribute('ffw-isinitialised', 'true');
            componentRootNode.setAttribute('ffw-increment', '0');

            // Adding missing menus to pre-created rows
            let rowNodesList = componentRootNode.querySelectorAll('[ffw-target="' + TArrayInputComponent.componentType + '.Row"]');
            if (rowNodesList.length > 0) {
                let firstRowNode = componentRootNode.querySelector('[ffw-target="' + TArrayInputComponent.componentType + '.Row"]');

                // Adding menu line
                this.#insertMenuLine(componentRootNode, firstRowNode.previousElementSibling, true);

                let me = this;
                rowNodesList.forEach(function (item) {
                    me.#insertMenuLine(componentRootNode, item, false);
                });
            } else {
                // Adding menu line
                this.#insertMenuLine(componentRootNode, null, true);
            }
        } catch (exp) {
            console.error(exp)
        }
    }

    addRecord(fieldValuesList = [], recordKey = null) {
        try {
            // Input checking
            if (!Array.isArray(fieldValuesList)) {
                throw 'The "fieldValuesList" variable is not an array!';
            }

            // Insert new row
            this.#insertNewRowNode(this.rootNode, null, fieldValuesList, recordKey);

        } catch (exp) {
            console.error(exp)
        }
    }

    removeAllRecords() {
        // Remove menu-line nodes
        let menuLineNodesList = document.body.querySelectorAll('[ffw-target="' + TArrayInputComponent.componentType + '.MenuLine"]:not([ffw-isinitial="true"])');
        if (menuLineNodesList === null || menuLineNodesList === undefined) {
            throw 'The list is null or undefined!';
        }
        menuLineNodesList.forEach(function (menuLineNode) {
            parent = menuLineNode.parentNode;
            parent.removeChild(menuLineNode);
        });

        // Remove record nodes
        let rowNodesList = document.body.querySelectorAll('[ffw-target="' + TArrayInputComponent.componentType + '.Row"]');
        if (rowNodesList === null || rowNodesList === undefined) {
            throw 'The list is null or undefined!';
        }
        rowNodesList.forEach(function (rowNode) {
            parent = rowNode.parentNode;
            parent.removeChild(rowNode);
        });
    }

    constructor(componentRootNode) {

        this.rootNode = null;
        this.ffwAddButtonCaption = "New line";
        this.ffwRemoveButtonCaption = "Remove line";

        // Checking input parameters
        if (componentRootNode === null || componentRootNode === undefined) {
            throw 'The component(root) node is null or undefined!';
        }

        // Set variables
        this.rootNode = componentRootNode;

        this.#initialiseComponent(componentRootNode);
    }

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// CONFIRM   ////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

$(document).ready(function () {
    'use strict';

    let multiLayer = function () {

        // Find backdrop nodes
        let backdropsList = document.body.querySelectorAll('div[class^="modal-backdrop"]');
        if (backdropsList !== null && backdropsList !== undefined) {
            var index=1049;
            backdropsList.forEach(function (item) {
                console.log('backdrop:'+index);
                item.style.zIndex = index;
                index +=2;
            });
        }

        const modalsList = document.getElementById("modalcontainer").children;
        if (modalsList !== null && modalsList !== undefined) {
            var index=1050;

            for (let i = 0; i < modalsList.length; i++) {
                modalsList[i].style.zIndex = index;
                index +=2;
            }
        }


        // Find modal nodes
        // let modalsList = document.body.querySelectorAll('div[class^="modal"]:not([class^="modal-backdrop"])');
        // if (modalsList !== null && modalsList !== undefined) {
        //     let index=1050;
        //     modalsList.forEach(function (item) {
        //         console.log('modal:'+index);
        //         item.style.zIndex = index;
        //         index++;
        //     });
        // }

    }

    let layering=function() {
        multiLayer();
        setTimeout(multiLayer,50);
        setTimeout(multiLayer,100);
        setTimeout(multiLayer,200);
        setTimeout(multiLayer,300);
        setTimeout(multiLayer,400);
        setTimeout(multiLayer,500);
        setTimeout(multiLayer,1000);
        setTimeout(multiLayer,3000);
        setTimeout(multiLayer,5000);
    }


    let successResponseHandler = function(response){
        $('#modalcontainer').append(response);
        layering();
    }



    let successResponseHandler_old = function (response) {
        switch (response.type) {
            case 'list':
                $('#content').html(response.content);
                if($('body').hasClass("modal-open")) {
                    $('body').removeClass("modal-open");
                }

                break;
            case 'modal':
                if ($('#' + response.id).length) {
                    $('#' + response.id).modal('show');
                } else {
                    $('#modalcontainer').append(response.content);
                }
                break;
            case 'reload':
                location.href = response.content;
                break;
            case 'logout':
                location.href = response.content;
                break;
            default:
                showMessage(response.type, response.content);
        }
        layering();

    }

    let errorResponseHandler = function (response) {
        layering();
        console.error(response);
    }

    let drawAttributeIfNotNull = function (attributeName, attributeValue, defaultAttributeValue) {
        // Input checking
        if (attributeName === undefined) {
            throw 'The "attributeName" parameter must not be undefined!';
        }
        if (attributeName == null) {
            throw 'The "attributeName" parameter must not be null!';
        }
        if (defaultAttributeValue !== undefined && defaultAttributeValue == null) {
            throw 'The "defaultAttributeValue" parameter must not be null!';
        }

        // Generating output
        if (attributeValue != null) {
            return ' ' + attributeName + '="' + attributeValue + '" ';
        } else if (attributeValue == null && defaultAttributeValue !== undefined) {
            return ' ' + attributeName + '="' + defaultAttributeValue + '" ';
        }

        return "";
    }
    let drawContent = function (content, defaultContent) {
        // Input checking
        if (defaultContent !== undefined && defaultContent == null) {
            throw 'The "defaultContent" parameter must not be null!';
        }

        // Generating output
        if (content != null) {
            return content;
        } else if (content == null && defaultContent !== undefined) {
            return defaultContent;
        }

        return "";
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////// CONFIRM LINK  ////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // $(document).on('click', '[ffw-type="ConfirmCustomAction"]', function (e) {
    //         e.preventDefault();
    //         var self = this;
    //         try {
    //             // Initialise default values
    //             let defaultDialogTitle = 'Megerősítés';
    //             let defaultDialogMessage = 'Folytatja a megkezdett műveletet?';
    //             let defaultDialogYesButtonCaption = 'Igen';
    //             let defaultDialogNoButtonCaption = 'Nem';
    //
    //             // Initialise obligatory ffw attributes
    //             let ffwAction = this.getAttribute("ffw-action");
    //
    //             // Initialise optional ffw attributes
    //             let ffwDialogId = this.getAttribute("ffw-dialog-id");
    //             let ffwDialogMessage = this.getAttribute("ffw-dialog-message");
    //             let ffwDialogTitle = this.getAttribute("ffw-dialog-title");
    //             let ffwDialogYesButtonCaption = this.getAttribute("ffw-dialog-yesbutton-caption");
    //             let ffwDialogNoButtonCaption = this.getAttribute("ffw-dialog-nobutton-caption");
    //             let ffwDialogAttributesId = this.getAttribute("ffw-dialog-attributes-id");
    //             let ffwDialogAttributesName = this.getAttribute("ffw-dialog-attributes-name");
    //
    //             // Input checking for attribute existence
    //             if (ffwAction == null) {
    //                 throw "The request-action is not determined!";
    //             }
    //
    //
    //             // Set default value
    //             if (ffwDialogId == null) {
    //                 ffwDialogId = 'D' + Date.now();
    //             }
    //
    //             // Draw: YesButton
    //             let buttonStr = '' +
    //                 '<button ' +
    //                 'ffw-alias="YesButton" ' +
    //                 'type="button" ' +
    //                 'class="btn bg-danger" ' +
    //                 'ffw-action="' + ffwAction + '" >' +
    //                 drawContent(ffwDialogYesButtonCaption, defaultDialogYesButtonCaption) +
    //                 '</button>';
    //
    //
    //             // Main draw
    //             let dialogStr = '' +
    //                 '<div class="modal fade" data-backdrop="static" data-keyboard="false" ' +
    //                 drawAttributeIfNotNull("id", ffwDialogAttributesId) +
    //                 drawAttributeIfNotNull("name", ffwDialogAttributesName) +
    //                 drawAttributeIfNotNull("ffw-dialog-id", ffwDialogId) +
    //                 ' aria-hidden="true">' +
    //                 '<div class="modal-dialog">' +
    //                 '<div class="modal-content">' +
    //                 '<div class="modal-header bg-danger ">' +
    //                 '<h4 class="modal-title">' +
    //                 drawContent(ffwDialogTitle, defaultDialogTitle) +
    //                 '</h4>' +
    //                 '<button ffw-alias="CloseButton" type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>' +
    //                 '</div>' +
    //                 '<div class="modal-body text-center">' +
    //                 '<p>' +
    //                 drawContent(ffwDialogMessage, defaultDialogMessage) +
    //                 '</p>' +
    //                 '</div>' +
    //                 '<div class="modal-footer">' +
    //                 '<button ffw-alias="NoButton" type="button" class="btn btn-default focus" data-dismiss="modal">' +
    //                 drawContent(ffwDialogNoButtonCaption, defaultDialogNoButtonCaption) +
    //                 '</button>' +
    //                 buttonStr +
    //                 '</div>' +
    //                 '</div>' +
    //                 '</div>' +
    //                 '</div>';
    //
    //             $('#modalcontainer').append(dialogStr);
    //
    //             $('[ffw-dialog-id=' + ffwDialogId + ']').modal('show');
    //
    //             $('[ffw-dialog-id=' + ffwDialogId + ']').on('shown.bs.modal', function () {
    //                 $('[ffw-dialog-id=' + ffwDialogId + '] .focus').focus();
    //
    //                 const zIndex = 1040 + 10 * $('.modal:visible').length;
    //                 $(this).css('z-index', zIndex);
    //                 setTimeout(() => $('.modal-backdrop').not('.modal-stack').last().css('z-index', zIndex - 1).addClass('modal-stack'));
    //             });
    //
    //
    //             $('[ffw-dialog-id=' + ffwDialogId + ']').on('click', "[ffw-alias='YesButton']", function (event) {
    //
    //                 let buttonNode = event.target;
    //                 let ffwActionName = buttonNode.getAttribute('ffw-action');
    //                 if (ffwActionName == null) {
    //                     throw "The request-action is not determined!";
    //                 }
    //                 alert(3336);
    //                 $(event.target).trigger('ffwActionName');
    //
    //                 // Closing the dialog
    //                 $(self).trigger('onConfirmDialogClose');
    //                 $('[ffw-dialog-id=' + ffwDialogId + ']').data('bs.modal', null);
    //                 $("[ffw-dialog-id='" + ffwDialogId + "']").remove();        // Removes the dialog from modal container
    //                 $('.modal-backdrop:last-child').remove();                   // Removes the belonging dark-layer
    //
    //             });
    //
    //
    //         } catch (exp) {
    //             console.error(exp)
    //         }
    //     }
    // );


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////// CONFIRM SUBMIT  ////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    $(document).on('click', '[ffw-type="ConfirmSubmit"]', function (e) {
            e.preventDefault();
            var self = this;
            try {
                // Initialise default values
                let defaultDialogTitle = 'Megerősítés';
                let defaultDialogMessage = 'Folytatja a megkezdett műveletet?';
                let defaultDialogYesButtonCaption = 'Igen';
                let defaultDialogNoButtonCaption = 'Nem';

                // Initialise obligatory ffw attributes
                let ffwSourceFormId = this.getAttribute("ffw-source-formId");   // Obligatory only in some cases

                // Initialise optional ffw attributes
                let ffwDialogId = this.getAttribute("ffw-dialog-id");
                let ffwDialogMessage = this.getAttribute("ffw-dialog-message");
                let ffwDialogTitle = this.getAttribute("ffw-dialog-title");
                let ffwDialogYesButtonCaption = this.getAttribute("ffw-dialog-yesbutton-caption");
                let ffwDialogNoButtonCaption = this.getAttribute("ffw-dialog-nobutton-caption");
                let ffwDialogAttributesId = this.getAttribute("ffw-dialog-attributes-id");
                let ffwDialogAttributesName = this.getAttribute("ffw-dialog-attributes-name");

                // Input checking for attribute existence
                if (ffwSourceFormId == null) {
                    throw "The source-formId is not determined!";
                }

                // Set default value
                if (ffwDialogId == null) {
                    ffwDialogId = 'D' + Date.now();
                }

                // Draw: YesButton
                let buttonStr = '' +
                    '<button ' +
                    'ffw-alias="YesButton" ' +
                    'type="button" ' +
                    'class="btn bg-danger" ' +
                    'ffw-source-formId="' + ffwSourceFormId + '">' +
                    drawContent(ffwDialogYesButtonCaption, defaultDialogYesButtonCaption) +
                    '</button>';

                // Main draw
                let dialogStr = '' +
                    '<div class="modal fade" ' +
                    drawAttributeIfNotNull("id", ffwDialogAttributesId) +
                    drawAttributeIfNotNull("name", ffwDialogAttributesName) +
                    drawAttributeIfNotNull("ffw-dialog-id", ffwDialogId) +
                    ' aria-hidden="true">' +
                    '<div class="modal-dialog">' +
                    '<div class="modal-content">' +
                    '<div class="modal-header bg-danger ">' +
                    '<h4 class="modal-title">' +
                    drawContent(ffwDialogTitle, defaultDialogTitle) +
                    '</h4>' +
                    '<button ffw-alias="CloseButton" type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>' +
                    '</div>' +
                    '<div class="modal-body text-center">' +
                    '<p>' +
                    drawContent(ffwDialogMessage, defaultDialogMessage) +
                    '</p>' +
                    '</div>' +
                    '<div class="modal-footer">' +
                    '<button ffw-alias="NoButton" type="button" class="btn btn-default focus" data-dismiss="modal">' +
                    drawContent(ffwDialogNoButtonCaption, defaultDialogNoButtonCaption) +
                    '</button>' +
                    buttonStr +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>';

                $('#modalcontainer').append(dialogStr);

                $('[ffw-dialog-id=' + ffwDialogId + ']').modal('show');

                $('[ffw-dialog-id=' + ffwDialogId + ']').on('shown.bs.modal', function () {
                    $('[ffw-dialog-id=' + ffwDialogId + '] .focus').focus();

                    // Managing dark/covering-layer
                    const zIndex = 1040 + 10 * $('.modal:visible').length;
                    $(this).css('z-index', zIndex);
                    setTimeout(() => $('.modal-backdrop').not('.modal-stack').last().css('z-index', zIndex - 1).addClass('modal-stack'));
                });

                $('[ffw-dialog-id=' + ffwDialogId + ']').on('click', "[ffw-alias='YesButton']", function () {

                    let evt = new CustomEvent("submit", {"bubbles": true, "cancelable": true});
                    document.getElementById(ffwSourceFormId).dispatchEvent(evt)

                    // Closing the dialog
                    $(self).trigger('onConfirmDialogClose');
                    $('[ffw-dialog-id=' + ffwDialogId + ']').data('bs.modal', null);
                    $("[ffw-dialog-id='" + ffwDialogId + "']").remove();        // Removes the dialog from modal container
                    $('.modal-backdrop').last().remove();                   // Removes the belonging dark-layer

                });


            } catch (exp) {
                console.error(exp)
            }
        }
    );


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////// CONFIRM LINK  ////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    $(document).on('click', '[ffw-type="ConfirmLink"]', function (e) {
            e.preventDefault();
            var self = this;
            try {
                // Initialise default values
                let defaultDialogTitle = 'Megerősítés';
                let defaultDialogMessage = 'Folytatja a megkezdett műveletet?';
                let defaultDialogYesButtonCaption = 'Igen';
                let defaultDialogNoButtonCaption = 'Nem';

                // Initialise obligatory ffw attributes
                let ffwTarget = this.getAttribute("ffw-target");
                let ffwRequestMode = this.getAttribute("ffw-request-mode");
                let ffwSourceFormId = this.getAttribute("ffw-source-formId");   // Obligatory only in some cases

                // Initialise optional ffw attributes
                let ffwDialogId = this.getAttribute("ffw-dialog-id");
                let ffwDialogMessage = this.getAttribute("ffw-dialog-message");
                let ffwDialogTitle = this.getAttribute("ffw-dialog-title");
                let ffwDialogYesButtonCaption = this.getAttribute("ffw-dialog-yesbutton-caption");
                let ffwDialogNoButtonCaption = this.getAttribute("ffw-dialog-nobutton-caption");
                let ffwDialogAttributesId = this.getAttribute("ffw-dialog-attributes-id");
                let ffwDialogAttributesName = this.getAttribute("ffw-dialog-attributes-name");

                // Input checking for attribute existence
                if (ffwTarget == null) {
                    throw "The request-target is not determined!";
                }

                if (ffwRequestMode != null && ffwRequestMode != "GET" && ffwRequestMode != "POST" && ffwRequestMode != "DELETE") {
                    throw "The request-mode '" + ffwRequestMode + "' is not supported!";
                }

                if (ffwRequestMode == "POST" && ffwSourceFormId == null) {
                    throw "The source-formId is not determined!";
                }

                // Set default value
                if (ffwRequestMode == null) {
                    ffwRequestMode = "GET"
                }
                ffwRequestMode.toUpperCase();

                // Set default value
                if (ffwDialogId == null) {
                    ffwDialogId = 'D' + Date.now();
                }

                // Draw: YesButton
                let buttonStr = "";
                if (ffwRequestMode == "GET") {
                    buttonStr = '' +
                        '<button ' +
                        'ffw-alias="YesButton" ' +
                        'type="button" ' +
                        'class="btn bg-danger" ' +
                        'ffw-target="' + ffwTarget + '" ffw-request-mode="GET">' +
                        drawContent(ffwDialogYesButtonCaption, defaultDialogYesButtonCaption) +
                        '</button>';
                } else if (ffwRequestMode == "POST") {
                    buttonStr = '' +
                        '<button ' +
                        'ffw-alias="YesButton" ' +
                        'type="button" ' +
                        'class="btn bg-danger" ' +
                        'ffw-target="' + ffwTarget + '" ' +
                        'ffw-request-mode="POST" ' +
                        'ffw-source-formId="' + ffwSourceFormId + '">' +
                        drawContent(ffwDialogYesButtonCaption, defaultDialogYesButtonCaption) +
                        '</button>';
                } else if (ffwRequestMode == "DELETE") {
                    buttonStr = '' +
                        '<button ' +
                        'ffw-alias="YesButton" ' +
                        'type="button" ' +
                        'class="btn bg-danger" ' +
                        'ffw-target="' + ffwTarget + '" ' +
                        'ffw-request-mode="DELETE" ' +
                        'ffw-source-formId="' + ffwSourceFormId + '">' +
                        drawContent(ffwDialogYesButtonCaption, defaultDialogYesButtonCaption) +
                        '</button>';
                }


                // Main draw
                let dialogStr = '' +
                    '<div class="modal fade" data-backdrop="static" data-keyboard="false" ' +
                    drawAttributeIfNotNull("id", ffwDialogAttributesId) +
                    drawAttributeIfNotNull("name", ffwDialogAttributesName) +
                    drawAttributeIfNotNull("ffw-dialog-id", ffwDialogId) +
                    ' aria-hidden="true">' +
                    '<div class="modal-dialog">' +
                    '<div class="modal-content">' +
                    '<div class="modal-header bg-danger ">' +
                    '<h4 class="modal-title">' +
                    drawContent(ffwDialogTitle, defaultDialogTitle) +
                    '</h4>' +
                    '<button ffw-alias="CloseButton" type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>' +
                    '</div>' +
                    '<div class="modal-body text-center">' +
                    '<p>' +
                    drawContent(ffwDialogMessage, defaultDialogMessage) +
                    '</p>' +
                    '</div>' +
                    '<div class="modal-footer">' +
                    '<button ffw-alias="NoButton" type="button" class="btn btn-default focus" data-dismiss="modal">' +
                    drawContent(ffwDialogNoButtonCaption, defaultDialogNoButtonCaption) +
                    '</button>' +
                    buttonStr +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>';

                $('#modalcontainer').append(dialogStr);

                $('[ffw-dialog-id=' + ffwDialogId + ']').modal('show');

                $('[ffw-dialog-id=' + ffwDialogId + ']').on('shown.bs.modal', function () {
                    $('[ffw-dialog-id=' + ffwDialogId + '] .focus').focus();

                    const zIndex = 1040 + 10 * $('.modal:visible').length;
                    $(this).css('z-index', zIndex);
                    setTimeout(() => $('.modal-backdrop').not('.modal-stack').last().css('z-index', zIndex - 1).addClass('modal-stack'));
                });


                $('[ffw-dialog-id=' + ffwDialogId + ']').on('click', "[ffw-alias='YesButton']", function () {

                    if (ffwRequestMode == "GET") {
                        $.ajax({
                            type: 'get',
                            url: ffwTarget,
                            dataType: 'json',
                            async: true,
                            beforeSend: function(xhr) {
                                let csrfToken = $("meta[name='_csrf']").attr("content");
                                let csrfHeader = $("meta[name='_csrf_header']").attr("content");
                                xhr.setRequestHeader(csrfHeader, csrfToken); // Include CSRF token in request headers
                            },
                            success: function (response) {
                                $(self).trigger('onSuccessResponse', response);
                                successResponseHandler(response);
                            },
                            error: function (response) {
                                $(self).trigger('onErrorResponse', response);
                                errorResponseHandler(response);
                            }
                        });
                    } else if (ffwRequestMode == "POST") {
                        $.ajax({
                            type: 'post',
                            url: ffwTarget,
                            data: $('#' + ffwSourceFormId).serialize(),
                            dataType: 'json',
                            async: true,
                            beforeSend: function(xhr) {
                                let csrfToken = $("meta[name='_csrf']").attr("content");
                                let csrfHeader = $("meta[name='_csrf_header']").attr("content");
                                xhr.setRequestHeader(csrfHeader, csrfToken); // Include CSRF token in request headers
                            },
                            success: function (response) {
                                $(self).trigger('onSuccessResponse', response);
                                successResponseHandler(response);
                            },
                            error: function (response) {
                                $(self).trigger('onErrorResponse', response);
                                errorResponseHandler(response);
                            }
                        });
                    } else if (ffwRequestMode == "DELETE") {

                        $.ajax({
                            type: 'DELETE',
                            url: ffwTarget,
                            async: true,
                            beforeSend: function(xhr) {
                                let csrfToken = $("meta[name='_csrf']").attr("content");
                                let csrfHeader = $("meta[name='_csrf_header']").attr("content");
                                xhr.setRequestHeader(csrfHeader, csrfToken); // Include CSRF token in request headers
                            },
                            success: function (response) {
                                $(self).trigger('onSuccessResponse', response);
                                successResponseHandler(response);
                            },
                            error: function (response) {
                                $(self).trigger('onErrorResponse', response);
                                errorResponseHandler(response);
                            }
                        });
                    }


                    // Closing the dialog
                    $(self).trigger('onConfirmDialogClose');
                    $('[ffw-dialog-id=' + ffwDialogId + ']').data('bs.modal', null);
                    $("[ffw-dialog-id='" + ffwDialogId + "']").remove();        // Removes the dialog from modal container
                    $('.modal-backdrop:last-child').remove();                   // Removes the belonging dark-layer

                });


            } catch (exp) {
                console.error(exp)
            }
        }
    );

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////  LINK  /////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    $(document).on('click', '[ffw-type="Link"]', function (e) {
        e.preventDefault();
        var self = this;
        try {
            let ffwTarget = this.getAttribute("ffw-target");
            let ffwRequestMode = this.getAttribute("ffw-request-mode");
            let ffwSourceFormId = this.getAttribute("ffw-source-formId");
            let ffwResponseTarget = this.getAttribute("ffw-response-target");

            // Input checking
            if (ffwTarget == null) {
                throw "The request-target is not determined!";
            }

            // Set default values
            if (ffwRequestMode == null) {
                ffwRequestMode = "GET"
            }
            if (ffwResponseTarget == null) {
                ffwResponseTarget = "_SELF"
            }
            ffwRequestMode = ffwRequestMode.toUpperCase();
            ffwResponseTarget = ffwResponseTarget.toUpperCase();

            // Input checking
            if (ffwRequestMode != "GET" && ffwRequestMode != "POST" && ffwRequestMode != "DELETE") {
                throw "The request-mode '" + ffwRequestMode + "' is not supported!";
            }

            // Input checking
            if (ffwRequestMode == "POST" && ffwSourceFormId == null) {
                throw "The source-formId is not determined!";
            }

            // Input checking
            if (ffwResponseTarget != "_SELF" && ffwResponseTarget != "_BLANK") {
                throw "The response-target '" + ffwResponseTarget + "' is not supported!";
            }

            let csrfToken = $("meta[name='_csrf']").attr("content");
            let csrfHeader = $("meta[name='_csrf_header']").attr("content");

            if (ffwRequestMode == "GET") {
                if (ffwResponseTarget == "_SELF") {
                    $.ajax({
                        type: 'GET',
                        url: ffwTarget,
                        dataType: 'html',
                        async: true,
                        cache: false,
                        contentType: "application/json",
                        beforeSend: function(xhr) {
                            let csrfToken = $("meta[name='_csrf']").attr("content");
                            let csrfHeader = $("meta[name='_csrf_header']").attr("content");
                            xhr.setRequestHeader(csrfHeader, csrfToken); // Include CSRF token in request headers
                        },
                        success: function (response) {
                            $(self).trigger('onSuccessResponse', response);
                            successResponseHandler(response);
                        },
                        error: function (response) {
                            $(self).trigger('onErrorResponse', response);
                            errorResponseHandler(response);
                        }
                    });
                } else if (ffwResponseTarget == "_BLANK") {
                    window.open(ffwTarget, '_blank');
                }
            } else if (ffwRequestMode == "POST") {
                $.ajax({
                    type: 'POST',
                    url: ffwTarget,
                    data: $('#' + ffwSourceFormId).serialize(),
                    dataType: 'json',
                    async: true,
                    beforeSend: function(xhr) {
                        let csrfToken = $("meta[name='_csrf']").attr("content");
                        let csrfHeader = $("meta[name='_csrf_header']").attr("content");
                        xhr.setRequestHeader(csrfHeader, csrfToken); // Include CSRF token in request headers
                    },
                    success: function (response) {
                        $(self).trigger('onSuccessResponse', response);
                        successResponseHandler(response);
                    },
                    error: function (response) {
                        $(self).trigger('onErrorResponse', response);
                        errorResponseHandler(response);
                    }
                });
            }
            else if (ffwRequestMode == "DELETE") {
                    $.ajax({
                        type: 'DELETE',
                        url: ffwTarget,
                        dataType: 'html',
                        async: true,
                        cache: false,
                        contentType: "application/json",
                        beforeSend: function(xhr) {
                            let csrfToken = $("meta[name='_csrf']").attr("content");
                            let csrfHeader = $("meta[name='_csrf_header']").attr("content");
                            xhr.setRequestHeader(csrfHeader, csrfToken); // Include CSRF token in request headers
                        },
                        success: function (response) {
                            $(self).trigger('onSuccessResponse', response);
                            successResponseHandler(response);
                        },
                        error: function (response) {
                            $(self).trigger('onErrorResponse', response);
                            errorResponseHandler(response);
                        }
                    });

            }

        } catch (exp) {
            console.error(exp)
        }
    });


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////  DataTable.RefreshButton  /////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    $(document).on('click', '[ffw-type="DataTable.RefreshButton"]', function (e) {
        e.preventDefault();
        var self = this;
        try {
            let ffwTarget = this.getAttribute("ffw-target");

            // Input checking
            if (ffwTarget == null) {
                throw "The request-target is not determined!";
            }

            $('#' + ffwTarget).DataTable().ajax.reload();
        } catch (exp) {
            console.error(exp)
        }
    });


});