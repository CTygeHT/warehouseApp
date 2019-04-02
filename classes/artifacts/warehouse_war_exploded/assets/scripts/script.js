var serverHostName = window.location.hostname;

var serverProtocolName = window.location.protocol;

var portName = window.location.port;

var TmcListIndex;

var docNumber;

var markerForSalesRow;
var markerForMoveRow;

if (portName.length == 0) {
    portName = "80";
}

if (serverHostName === "localhost") {
    serverPath = serverProtocolName + "//" + serverHostName + ":" + portName;
}
else {
    serverPath = serverProtocolName + "//" + serverHostName;
}

function serverConnectFunc(serverUrl, jsonData) {
    $.ajax({
        url: serverUrl + "/",
        type: 'POST',
        data: jsonData,

        dataType: 'json',
        async: false,

        success: function (event) {
            switch (event["answer"]) {
                case "ok":
                    alert("success");
                    break;

                case "names":
                    var keysList = event["list"].replace("[", "").replace("]", "").split(", ");

                    $("#dataTableOut").empty();

                    for (var i = 0; i < (keysList.length - 1); i += 2) {

                        $("#dataTableOut").append("<tr><td style='text-align: left'>" + keysList[i] + "</td><td>" + keysList[i + 1] + "</td></tr>");
                    }

                    break;

                case "tmc_list":
                    var keysList = event["list"].replace("[", "").replace("]", "").split(", ");
                    $("#tmc_select" + TmcListIndex).empty();
                    var groupName = "";

                    var width = document.getElementById("tmc_select" + TmcListIndex).offsetWidth;
                    for (var i = 0; i < (keysList.length - 1); i += 3) {
                        if (keysList[i+2] !== groupName) {
                            var centerStr = "&nbsp−−==≡≡<< ";
                            for (var j = 0; j < ((width/7-keysList[i + 2].length) / 2); j++) {
                                centerStr = "&nbsp" + centerStr;
                            }

                            $("#tmc_select" + TmcListIndex).append("<option value=\"marker\"disabled>" +
                                 centerStr + keysList[i + 2] + " >>≡≡==−−</option>>");
                            groupName = keysList[i + 2];
                        }
                        $("#tmc_select" + TmcListIndex).append("<option value=\"" + keysList[i] + "\">" + keysList[i + 1] + "</option>>");
                    }

                    document.getElementById("tmc_select" + TmcListIndex).selectedIndex = -1;
                    break;

                case "cust_list":
                    var keysListCust = event["list"].replace("[", "").replace("]", "").split(", ");
                    $("#customer_select").empty();

                    for (var i = 0; i < (keysListCust.length - 2); i += 3) {
                        // <option value="1">Київ</option>
                        $("#customer_select").append("<option value=\"" + keysListCust[i] + "\">" + keysListCust[i + 1] + "</option>>");
                    }
                    document.getElementById("customer_select").selectedIndex = -1;
                    break;

                case "pant_list":
                    var keysList = event["list"].replace("[", "").replace("]", "").split(", ");
                    $("#pant_select").empty();

                    for (var i = 0; i < (keysList.length - 1); i += 2) {
                        // <option value="1">Київ</option>
                        $("#pant_select").append("<option value=\"" + keysList[i] + "\">" + keysList[i + 1] + "</option>>");
                    }
                    document.getElementById("pant_select").selectedIndex = -1;
                    break;

                case "formula":
                    var keysList = event["list"].replace("[", "").replace("]", "").split(", ");
                    $("#selectGr").empty();
                    $("#inputGr").empty();
                    $("#weightGr").empty();
                    $("#leftoverCheckGr").empty();
                    TmcListIndex = 0;

                    for (var ij = 0; ij < (keysList.length - 2); ij += 3) {
                        // <option value="1">Київ</option>
                        //if (!document.getElementById("tmc_select" + (ij / 5 + 1))) {
                        addRow();
                        getTmcList(ij / 3 + 1);
                        //}
                        $("#tmc_select" + (ij / 3 + 1)).val(keysList[ij]).change();
                        $("#input" + (ij / 3 + 1)).val(keysList[ij + 1]).change();
                        leftoverList[ij / 3 + 1] = keysList[ij + 2];
                    }

                    break;

                case "docNo" :
                    var keysList = event["No"];
                    docNumber = keysList + 1;

                    break;

                case "tmcLeftover" :
                    var keysList = event["number"];
                    leftoverList[markerForSalesRow] = keysList;

                    break;

                case "tmcLeftoverMove" :
                    var keysListIn = event["in"];
                    var keysListOut = event["Out"];
                    leftoverList[markerForMoveRow] = keysListOut;
                    $("#inputOut" + markerForMoveRow).val(keysListOut).change();
                    $("#inputIn" + markerForMoveRow).val(keysListIn).change();

                    break;

                case "wh_list":
                    var keysListCust = event["list"].replace("[", "").replace("]", "").split(", ");
                    $("#warehouse").empty();

                    for (var i = 0; i < (keysListCust.length - 1); i += 2) {
                        // <option value="1">Київ</option>
                        $("#warehouse").append("<option value=\"" + keysListCust[i] + "\">" + keysListCust[i + 1] + "</option>>");
                    }
                    document.getElementById("warehouse").selectedIndex = -1;
                    break;

                case "tableFull":
                    var keysList = event["list"].replace("[", "").replace("]", "").split(", ");
                    $("#dataTableOut").empty();

                    for (var i = 0; i < (keysList.length - 1); i += 6) {
                        $("#dataTableOut").append("<tr><td style='col-md-6 text-align: left'>" + keysList[i] + "</td>" +
                                                        "<td class='col-md-2'>" + keysList[i + 1] + "</td>" +
                                                        "<td class='col-md-2'>" + keysList[i + 3] + "</td>" +
                                                        "<td class='col-md-2'>" + keysList[i + 5] + "</td></tr>");
                    }

                    break;

                case "pant_listBlank":
                    var keysList = event["list"].replace("[", "").replace("]", "").split(", ");
                    $("#pant_select").empty();

                    for (var i = 0; i < (keysList.length - 1); i += 2) {
                        // <option value="1">Київ</option>
                        $("#pantName_select").append("<option value=\"" + keysList[i] + "\">" + keysList[i + 1] + "</option>>");
                    }
                    document.getElementById("pantName_select").selectedIndex = -1;
                    break;

                case "transaction":
                    var keysList = event["list"].replace("[", "").replace("]", "").split(", ");
                    $("#dataTableOut").empty();

                    for (var i = 0; i < (keysList.length - 1); i += 5) {
                        $("#dataTableOut").append(
                            "<tr><td>" + keysList[i] + "</td>" +
                            "<td>" + keysList[i + 1] + "</td>" +
                            "<td>" + keysList[i + 2] + "</td>" +
                            "<td>" + keysList[i + 3] + "</td>" +
                            "<td>" + keysList[i + 4] + "</td></tr>");

                    }

                    break;
            }
        },
        error: function (xhr, status, error) {
            alert(error);
        }
    });
}

function showTable() {
    var jsonData = new Object();
    jsonData.command = "0";
    jsonData.warehouse = $('#warehouse').val();
    //jsonData.date = $('#dateValue').val();

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function addTmc() {
    var jsonData = new Object();
    jsonData.command = "1";
    jsonData.name = $('#newTmcName').val();
    jsonData.comm = $('#newTmcComment').val();

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function getTmcList(listIndex) {
    var jsonData = new Object();
    jsonData.command = "2";
    TmcListIndex = listIndex;

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function addPurchase() {                                            //TODO rework
    getLastDocN();
    var queryString = '';
    var jsonData = new Object();
    jsonData.command = "3";
    jsonData.docNumber = docNumber + "";
    jsonData.warehouse = $('#warehouse').val() + "";
    jsonData.customer = $('#customer_select').val() + "";
    jsonData.count = TmcListIndex;
    for (var i = 1; i <= TmcListIndex; i++) {
        queryString += $('#tmc_select' + i).val() + ", ";
        queryString += $('#input' + i).val() + ", ";
    }
    jsonData.query = queryString;

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function getCustList() {
    var jsonData = new Object();
    jsonData.command = "4";

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function getTmcPantone() {
    var jsonData = new Object();
    jsonData.command = "5";

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function getFormula() {
    var jsonData = new Object();
    jsonData.command = "6";
    jsonData.pant = $('#pant_select').val();
    jsonData.customer = $('#customer_select').val();
    jsonData.warehouse = $('#warehouse').val();

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function addMixingOrder() {
    if (errorLeftover) {
        alert("невистачає фарби");
        return;
    }

    var jsonData = new Object();
    jsonData.command = "7";
    jsonData.pant = $('#pant_select').val();
    jsonData.sum = $('#sumWeight').val();
    jsonData.warehouse = $('#warehouse').val();

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function getLastDocN() {
    var jsonData = new Object();
    jsonData.command = "8";

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function getLeftover() {
    var jsonData = new Object();
    jsonData.command = "9";
    jsonData.warehouse = $('#warehouse').val() + "";
    jsonData.tmc = $('#'+ event.target.id).val();
    markerForSalesRow = event.target.id.replace("tmc_select", "");

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function addSale() {                                            //TODO rework
    for (var i = 1; i <= TmcListIndex; i++) {
        if(leftoverList[i] < $('#input' + i).val()) {
            alert("error");
            return;
        }
    }
    getLastDocN();
    var queryString = '';
    var jsonData = new Object();
    jsonData.command = "10";
    jsonData.docNumber = docNumber + "";
    jsonData.warehouse = $('#warehouse').val() + "";
    jsonData.customer = $('#customer_select').val() + "";
    jsonData.count = TmcListIndex;
    for (var i = 1; i <= TmcListIndex; i++) {
        queryString += $('#tmc_select' + i).val() + ", ";
        queryString += -$('#input' + i).val() + ", ";
    }
    jsonData.query = queryString;

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function addTrans(source, destination, salesMarker) {                                            //TODO rework
    if (salesMarker === -1) {
        for (var i = 1; i <= TmcListIndex; i++) {
            if (leftoverList[i] < $('#input' + i).val()) {
                alert("error");
                return;
            }
        }
    }

    getLastDocN();
    var queryString = '';
    var jsonData = new Object();
    jsonData.command = "10";
    jsonData.docNumber = docNumber + "";
    jsonData.warehouse = source + "";
    jsonData.customer = destination + "";
    jsonData.count = TmcListIndex;
    for (var i = 1; i <= TmcListIndex; i++) {
        queryString += $('#tmc_select' + i).val() + ", ";
        queryString += salesMarker * $('#input' + i).val() + ", ";
    }
    jsonData.query = queryString;

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}


function getWH() {
    var jsonData = new Object();
    jsonData.command = "11";

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function showTableFull() {
    var jsonData = new Object();
    jsonData.command = "12";

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function getLeftoverMove() {
    var jsonData = new Object();
    jsonData.command = "13";
    jsonData.warehouseIn = $('#customer_select').val() + "";
    jsonData.warehouseOut = $('#warehouse').val() + "";
    jsonData.tmc = $('#'+ event.target.id).val();
    markerForMoveRow = event.target.id.replace("tmc_select", "");

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));

}

function addReceipt() {

    var pantString = "";
    var jsonData = new Object();
    jsonData.command = "14";
    jsonData.customer = $('#customer_select').val();
    jsonData.pant = $('#pantName_select').val();
    jsonData.count = TmcListIndex;
    for (var i = 1; i <= TmcListIndex; i++) {
        pantString += $('#tmc_select' + i).val() + ", ";
        pantString += $('#input' + i).val() + ", ";
    }

    jsonData.receipt = pantString;

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function getTmcPantoneBlank() {
    var jsonData = new Object();
    jsonData.command = "15";

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function showTrans() {
    var jsonData = new Object();
    jsonData.command = "16";

    serverConnectFunc(serverProtocolName + "//" + serverHostName + ":" + portName, JSON.stringify(jsonData));
}

function addRow() {
    var divSelectTarget = document.getElementById("selectGr");
    var divInputTarget = document.getElementById("inputGr");
    var divInputWTarget = document.getElementById("weightGr");
    var divLeftoverChTarget = document.getElementById("leftoverCheckGr");
    var divSelect = document.createElement("div");
    var divInput = document.createElement("div");
    var divInputW = document.createElement("div");
    var divLeftoverCh = document.createElement("div");
    divSelect.className = "form-group";
    divInput.className = "form-group";
    divInputW.className = "form-group";
    divLeftoverCh.className = "form-group";
    items++;
    TmcListIndex++;
    divSelect.innerHTML = "<select class=\"form-control\" id=\"tmc_select" + TmcListIndex + "\" disabled></select>";
    divInput.innerHTML = "<input type=\"text\" class=\"form-control\" id=\"input" + TmcListIndex + "\" disabled>";
    divInputW.innerHTML = "<input type=\"text\" class=\"form-control\" id=\"inputW" + TmcListIndex + "\" disabled>";
    divLeftoverCh.innerHTML = "<button type=\"button\" disabled=\"disabled\" class=\"btn btn-success btn-circle-inline\"\n" +
        "id=\"leftoverCheck" + TmcListIndex + "\">\n" +
        "<i class=\"fa fa-times\" id=\"leftoverCheckTxt" + TmcListIndex + "\"></i>\n" +
        "</button>";
    divSelectTarget.appendChild(divSelect);
    divInputTarget.appendChild(divInput);
    divInputWTarget.appendChild(divInputW);
    divLeftoverChTarget.appendChild(divLeftoverCh);

}

function addRowMove() {
    var divSelectTarget = document.getElementById("selectGr");
    var divInputTarget = document.getElementById("inputGr");
    var divInputOutTarget = document.getElementById("weightGrOut");
    var divInputInTarget = document.getElementById("weightGrIn");
    var divSelect = document.createElement("div");
    var divInput = document.createElement("div");
    var divInputOut = document.createElement("div");
    var divInputIn = document.createElement("div");
    items++;
    TmcListIndex++;
    divSelect.className = "form-group";
    divInput.className = "form-group has-success";
    divInput.id = "div" + TmcListIndex;
    divInputOut.className = "form-group";
    divInputIn.className = "form-group";
    divSelect.innerHTML = "<select class=\"form-control\" id=\"tmc_select" + TmcListIndex + "\" ></select>";
    divInput.innerHTML = "<input type=\"text\" class=\"form-control\" id=\"input" + TmcListIndex + "\" >";
    divInputOut.innerHTML = "<input type=\"text\" class=\"form-control\" id=\"inputOut" + TmcListIndex + "\" disabled>";
    divInputIn.innerHTML = "<input type=\"text\" class=\"form-control\" id=\"inputIn" + TmcListIndex + "\" disabled>";

    divSelectTarget.appendChild(divSelect);
    divInputTarget.appendChild(divInput);
    divInputOutTarget.appendChild(divInputOut);
    divInputInTarget.appendChild(divInputIn);

}

function addRowFormula() {
    var divSelectTarget = document.getElementById("selectGr");
    var divInputTarget = document.getElementById("inputGr");
    var divSelect = document.createElement("div");
    var divInput = document.createElement("div");
    divSelect.className = "form-group";
    divInput.className = "form-group";
    items++;
    TmcListIndex++;
    divSelect.innerHTML = "<select class=\"form-control\" id=\"tmc_select" + TmcListIndex + "\"></select>";
    divInput.innerHTML = "<input type=\"text\" class=\"form-control\" id=\"input" + TmcListIndex + "\">";
    divSelectTarget.appendChild(divSelect);
    divInputTarget.appendChild(divInput);

}
function addRowPurch() {
    var divSelectTarget = document.getElementById("selectGr");
    var divInputTarget = document.getElementById("inputGr");
    var divSelect = document.createElement("div");
    var divInput = document.createElement("div");
    items++;
    TmcListIndex++;
    divSelect.className = "form-group";
    divInput.className = "form-group has-success";
    divInput.id = "div" + TmcListIndex;
    divSelect.innerHTML = "<select class=\"form-control\" id=\"tmc_select" + TmcListIndex + "\"></select>";
    divInput.innerHTML = "<input type=\"text\" class=\"form-control has-success\" id=\"input" + TmcListIndex + "\">";
    divSelectTarget.appendChild(divSelect);
    divInputTarget.appendChild(divInput);

}

function allTmc() {
    for (var i = 0; i < 210; i++) {
        AddRowPurch();
        getTmcList(TmcListIndex);
        if ($("#tmc_select" + TmcListIndex).val != "marker") {
            $("#tmc_select" + TmcListIndex).val(TmcListIndex).change();
        }
    }
}

function checkPortion() {
    var count = 0;

    var iter = 1;
    while (document.getElementById("input" + iter)) {
        count += Math.round(Number(document.getElementById("input" + iter).value)*100);
        iter++;
    }
    if(count/100 != 100) {
         document.getElementById("inputGr").classList.remove("has-success");
         document.getElementById("inputGr").classList.add("has-error");
    }
    else {
        document.getElementById("inputGr").classList.add("has-success");
        document.getElementById("inputGr").classList.remove("has-error");
    }
}

function checkLeftover() {
    var curInputRow = event.target.id.replace("input", "");

    if(leftoverList[curInputRow] < $('#'+ event.target.id).val()) {
         document.getElementById("div" + curInputRow).classList.remove("has-success");
         document.getElementById("div" + curInputRow).classList.add("has-error");
    }
    else {
        document.getElementById("div" + curInputRow).classList.add("has-success");
        document.getElementById("div" + curInputRow).classList.remove("has-error");
    }
}

function PortionsWeight() {

    var iter = 1;
    errorLeftover = false;
    while (document.getElementById("inputW" + iter)) {
        document.getElementById("inputW" + iter).value = Number(document.getElementById("sumWeight").value *
            Number(document.getElementById("input" + iter).value) / 100);
        if (Number(leftoverList[iter]) < Number(document.getElementById("inputW" + iter).value)) {
            errorLeftover = true;
            document.getElementById("leftoverCheck" + iter).classList.remove("btn-success");
            document.getElementById("leftoverCheck" + iter).classList.add("btn-danger");
        } else {
            document.getElementById("leftoverCheck" + iter).classList.remove("btn-danger");
            document.getElementById("leftoverCheck" + iter).classList.add("btn-success");
        }
        iter++;
    }

}