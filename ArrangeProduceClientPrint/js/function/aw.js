$(document).on("click", ".aw-arrange-print", function () {
    let awArrangeId = $(this).attr("aw-arrange-id");
    getAwArrangeQrCode(awArrangeId, this);
    $("#load-modal-aw").modal("show");
});

function getAwArrangeQrCode(awArrangeId, dom) {
    $.ajax({
        url: serverUrl + "/aw/" + awArrangeId,
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                /*$("#qr-code-aw").attr("src", result.data);*/
                qrCode = result.data;
                getData(dom, result.data);
                getAllAwArrange(0, 20);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function getData(dom, qrCode) {
    $("#print-btn-arrange").empty();

    const tr = $(dom).parents("tr");
    let creator = $(dom).attr("creator");

    let machine = tr.find("td:eq(1)").text();
    let groupNumber = tr.find("td:eq(2)").text();
    let gauge = tr.find("td:eq(3)").text();
    let positiveTolerance = tr.find("td:eq(4)").text();
    let negativeTolerance = tr.find(("td:eq(5)")).text();
    let arrangeDate = tr.find("td:eq(6)").text();
    let shift = tr.find("td:eq(8)").text();

    let machineNumber = tr.find("td:eq(1)").attr("machine-number");

    let ra = new RegExp("A", "g");
    let rb = new RegExp("B", "g");

    let str = machineNumber.slice(0, 1);
    let groupSide = groupNumber.slice(0, 1);
    if (str === "t") {
        let groupNow = groupNumber.replace(ra, "").replace(rb, "").split("-");
        let groupNumbers = [];
        for (i = parseInt(groupNow[0]); i <= parseInt(groupNow[1]); i++) {
            let test = i < 10 ? "0" + i : i;
            groupNumbers.push(groupSide + test);
        }
        $.each(groupNumbers, function (index, item) {
            let rowDiv = $("<div class='row' style='page-break-after:always;'></div>");
            let qrCodeDiv = $("<div class='col-lg-6 col-sm-6'></div>").append($("<img>").attr("src", qrCode[index]));
            let dataDiv = $("<div class='col-lg-6 col-ms-6'></div>");

            let machineH = $("<h3>设备型号&nbsp;: </h3>").append($("<span>  </span>").append(machine));
            let groupH = $("<h3></h3>").append("放线编码 : " + item);
            let gaugeH = $("<h3>规格&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(gauge));
            let positiveToleranceH = $("<h3>正公差&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(positiveTolerance));
            let negativeToleranceH = $("<h3>负公差&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(negativeTolerance));
            let creatorH = $("<h3>排单人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(creator));
            let shiftH = $("<h3>班次&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(shift));
            let arrangeDataH = $("<h3>生产时间&nbsp;: </h3>").append($("<span></span>").append(arrangeDate));

            dataDiv.append(machineH)
                .append(groupH)
                .append(gaugeH)
                .append(positiveToleranceH)
                .append(negativeToleranceH)
                .append(creatorH)
                .append(shiftH)
                .append(arrangeDataH);
            rowDiv.append(qrCodeDiv).append(dataDiv).appendTo("#print-btn-arrange");
        });
    } else if (str === "s") {
        let groupNow = groupNumber.replace(ra, "").replace(rb, "").split("-");
        let groupNumbers = [];
        for (i = parseInt(groupNow[0]); i <= parseInt(groupNow[1]); i++) {
            let test = i < 10 ? "0" + i : i;
            groupNumbers.push(groupSide + test);
        }
        $.each(groupNumbers, function (index, item) {
            let rowDiv = $("<div class='row' style='page-break-after:always;'></div>");
            let qrCodeDiv = $("<div class='col-lg-6 col-sm-6'></div>").append($("<img>").attr("src", qrCode[index]));
            let dataDiv = $("<div class='col-lg-6 col-ms-6'></div>");

            let machineH = $("<h3>设备型号&nbsp;: </h3>").append($("<span>  </span>").append(machine));
            let groupH = $("<h3></h3>").append("收线编码 : " + item);
            let gaugeH = $("<h3>规格&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(gauge));
            let positiveToleranceH = $("<h3>正公差&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(positiveTolerance));
            let negativeToleranceH = $("<h3>负公差&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(negativeTolerance));
            let creatorH = $("<h3>排单人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(creator));
            let shiftH = $("<h3>班次&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(shift));
            let arrangeDataH = $("<h3>生产时间&nbsp;: </h3>").append($("<span></span>").append(arrangeDate));

            dataDiv.append(machineH)
                .append(groupH)
                .append(gaugeH)
                .append(positiveToleranceH)
                .append(negativeToleranceH)
                .append(creatorH)
                .append(shiftH)
                .append(arrangeDataH);
            rowDiv.append(qrCodeDiv).append(dataDiv).appendTo("#print-btn-arrange");
        });
    }
}