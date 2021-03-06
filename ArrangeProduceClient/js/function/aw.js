function getAllMachineWinding() {
    $.ajax({
        url: serverUrl + "/machine/winding",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                showMachineWinding(result.data);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function showMachineWinding(data) {
    $("#machine-aw").empty();
    $.each(data, function (index, item) {
        const option = $("<option></option>").append(item.machineName).attr("value", item.machineNumber);
        $("#machine-aw").append(option);
    })
}

// 退火/绕线机设置

$(document).on("click", "#machine-setting-aw", function () {
    $("#modal-area-aw").load("/sub/machine.html");
    getAllMachine(machineSort);
    $("#load-modal-aw").modal("show");
});

/**
 * 删除设备
 */
$(document).on("click", ".delete-machine", function () {
    const machineId = $(this).attr("machine-id");
    Notiflix.Confirm.Show("警告", "是否确认删除", "确定", "取消", function () {
        $.ajax({
            url: serverUrl + "/machine/" + machineId,
            contentType: "application/json; charset=utf-8",
            type: "DELETE",
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("token", token);
            },
            success: function (result) {
                if (result.status === 0) {
                    if (machineSort === "draw") {
                        getAllDraw();
                    } else {
                        getAllMachine(machineSort);
                        if (machineSort === "annealing") {
                            getAllMachineAnnealing();
                        } else if (machineSort === "winding") {
                            getAllMachineWinding();
                        }
                    }
                    Notiflix.Notify.Success(result.msg);
                } else {
                    Notiflix.Notify.Failure(result.msg);
                }
            }
        });
    });
});

/**
 * 删除规格
 */
$(document).on("click", ".delete-gauge", function () {
    const gaugeId = $(this).attr("gauge-id");
    Notiflix.Confirm.Show("警告", "是否确认删除", "确定", "取消", function () {
        $.ajax({
            url: serverUrl + "/gauge/" + gaugeId,
            contentType: "application/json; charset=utf-8",
            type: "DELETE",
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("token", token);
            },
            success: function (result) {
                if (result.status === 0) {
                    Notiflix.Notify.Success("删除成功");
                    getAllGauge();
                    getAllGaugeSelected();
                } else {
                    Notiflix.Notify.Failure(result.msg);
                }
            }
        })
    });
});

// ==================== 添加退火/绕线机排产 ===================== //

/**
 * 保存按钮点击事件
 */
$(document).on("click", "#save-btn-aw", function () {
    let data = getDataAw();
    if (data !== false) {
        saveAwArrange(data);
    }
});

/**
 * 打印按钮点击事件
 */
$(document).on("click", "#print-btn-aw", function () {
    let data = getDataAw();
    if (data !== false) {
        $("#modal-area").load("/sub/print.html");
        // saveAndPrint(data);
        $("#load-modal").modal("show");
    }
});

/**
 * 重置按钮
 */
$(document).on("click", "#reset-btn-aw", function () {
    $("#arrange-date-aw").val(printTimeFormat(new Date()));
    $("#positive-tolerance-aw").val("");
    $("#negative-tolerance-aw").val("");
});

function getDataAw() {
    let gauge = $("#gauge-aw option:selected").val();
    let machine = $("#machine-aw option:selected").val();
    let arrangeDate = new Date($('#arrange-date-aw').val()).getTime();
    let shift = $("input[name='shift-aw']:checked").val();

    let negativeTolerance = $("#negative-tolerance").val();
    let positiveTolerance = $("#positive-tolerance").val();
    let group = $("#group option:selected").val();

    return {
        gauge,
        machine,
        arrangeDate,
        shift,
        "sort": machineSort,
        negativeTolerance,
        positiveTolerance,
        group
    };
}

/**
 * 保存
 * @param data
 */
function saveAwArrange(data) {
    $.ajax({
        url: serverUrl + "/aw",
        contentType: "application/json; charset=utf-8",
        type: "POST",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        data: JSON.stringify(data),
        success: function (result) {
            if (result.status === 0) {
                getAllAwArrange(0, 20);
                Notiflix.Notify.Success("新建排产任务成功");
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

/**
 * 删除排产信息
 */
$(document).on("click", ".delete-arrange-aw", function () {
    let awArrangeId = $(this).attr("aw-arrange-id");
    Notiflix.Confirm.Show("警告", "是否确认删除", "确定", "取消", function () {
        $.ajax({
            url: serverUrl + "/aw/" + awArrangeId,
            contentType: "application/json; charset=utf-8",
            type: "DELETE",
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("token", token);
            },
            success: function (result) {
                if (result.status === 0) {
                    Notiflix.Notify.Success(result.msg);
                    getAllAwArrange(0, 20);
                } else {
                    Notiflix.Notify.Failure(result.msg);
                }
            }
        });
    });
});

/**
 * 全选
 */
$(document).on("click", "#ck-aw", function () {
    const arr = document.getElementsByName('good-aw');
    const ck = document.getElementById('ck-aw');
    for (i in arr) {
        arr[i].checked = ck.checked; // 全选
        //arr[i].checked=!arr[i].checked; 反向全选
    }
});

/**
 * 推送
 */
$(document).on("click", "#push-aw", function () {
    const awArrangeIdList = [];
    $("input[name='good-aw']:checked").each(function (index, item) {
        awArrangeIdList.push($(this).val());
    });
    if (awArrangeIdList.length === 0) {
        Notiflix.Notify.Warning("请选勾线要推送的排产信息");
    } else {
        $.ajax({
            url: serverUrl + "/aw",
            contentType: "application/json; charset=utf-8",
            type: "PUT",
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("token", token);
            },
            data: JSON.stringify(awArrangeIdList),
            success: function (result) {
                if (result.status === 0) {
                    Notiflix.Notify.Success("推送成功");
                    getAllAwArrange(0, 20);
                    socket.send("push a new aw arrange");
                } else {
                    Notiflix.Notify.Failure(result.msg);
                }
            }
        });
    }
});

/**
 * 查看线规详情
 */
$(document).on("click", ".aw-arrange-detail", function () {
    getAwArrangeDetail(this);
    $("#aw-arrange-detail-modal").modal("show");
});

function getAwArrangeDetail(dom) {
    let printStatus = $(dom).attr("print-status");
    let creator = $(dom).attr("creator");
    let createTime = $(dom).attr("create-time");
    let arrangeId = $(dom).attr("arrange-id");

    const tr = $(dom).parents("tr");

    let groupNumber = tr.find("td:eq(3)").text();
    let gauge = tr.find("td:eq(4)").text();
    let positiveTolerance = tr.find("td:eq(5)").text();
    let negativeTolerance = tr.find("td:eq(6)").text();
    let arrangeDate = tr.find("td:eq(7)").text();
    let shift = tr.find("td:eq(8)").text();
    let machine = tr.find("td:eq(2)").attr("machine-name");
    let machineNumber = tr.find("td:eq(2)").text();
    let push = tr.find("td:eq(9)").text();

    let data = {
        printStatus,
        creator,
        createTime,
        gauge,
        positiveTolerance,
        negativeTolerance,
        shift,
        machine,
        arrangeDate,
        push,
        groupNumber,
        machineNumber
    };

    $("#detail-gauge-aw").val(gauge);
    $("#detail-positive-tolerance-aw").val(positiveTolerance);
    $("#detail-negative-tolerance-aw").val(negativeTolerance);
    $("#detail-arrange-time-aw").val(arrangeDate);
    $("#detail-shift-aw").val(shift);
    $("#detail-creator-aw").val(creator);
    $("#detail-create-time-aw").val(printTimeFormatComplete(parseInt(createTime)));
    $("#detail-push-aw").val(push);
    $("#detail-machine-aw").val(machine);
    $("#detail-machine-aw").attr("machine-number", machineNumber);
    $("#detail-group-number-aw").val(groupNumber);
    if (printStatus === "0") {
        $("#detail-print-aw").val("未打印");
    } else {
        $("#detail-print-aw").val("已打印");
    }

    $("#print-history-aw").attr("arrange-id", arrangeId);
    $("#modify-aw").attr("arrange-id", arrangeId);
}

/**
 * 在列表中打印
 */
$(document).on("click", ".print-arrange-aw-btn", function () {
    let awArrangeId = $(this).attr("arrange-id");

    const tr = $(this).parents("tr");
    let machine = tr.find("td:eq(2)").attr("machine-name");
    let machineNumber = tr.find("td:eq(2)").text();
    let groupNumber = tr.find("td:eq(3)").text();
    let gauge = tr.find("td:eq(4)").text();
    let positiveTolerance = tr.find("td:eq(5)").text();
    let negativeTolerance = tr.find("td:eq(6)").text();
    let arrangeDate = tr.find("td:eq(7)").text();
    let shift = tr.find("td:eq(8)").text();
    let rawMaterial = tr.find("td:eq(10)").find("button:eq(1)").attr("raw-materials");
    let creator = tr.find("td:eq(10)").find("button:eq(1)").attr("creator");

    let data = {
        gauge,
        positiveTolerance,
        negativeTolerance,
        shift,
        machine,
        machineNumber,
        arrangeDate,
        rawMaterial,
        creator,
        groupNumber
    };

    $.ajax({
        url: serverUrl + "/aw/" + awArrangeId,
        contentType: "application/json; charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                generateQrCodeByHistoryAw(data, result.data);
                $("#detail-print-modal").modal("show");
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
});

/**
 * 生成二维码
 * @param data 数据
 * @param qrCode
 */
function generateQrCodeByHistoryAw(data, qrCode) {
    $("#print-btn-aw").empty();

    let ra = new RegExp("A", "g");
    let rb = new RegExp("B", "g");

    // 获取设备编码第一个字符
    let str = data.machineNumber.slice(0, 1);
    let groupSide = data.groupNumber.slice(0, 1);
    if (str === "t" || str === "f") {
        let groupNow = data.groupNumber.replace(ra, "").replace(rb, "").split("-");
        let groupNumbers = [];
        for (i = parseInt(groupNow[0]); i <= parseInt(groupNow[1]); i++) {
            let test = i < 10 ? "0" + i : i;
            groupNumbers.push(groupSide + test);
        }
        $.each(groupNumbers, function (index, item) {
            let rowDiv = $("<div class='row' style='page-break-after:always;'></div>");
            let qrCodeDiv = $("<div class='col-lg-6 col-sm-6'></div>").append($("<img>").attr("src", qrCode[index]));
            let dataDiv = $("<div class='col-lg-6 col-ms-6'></div>");

            let machineH = $("<h3>设备型号&nbsp;: </h3>").append($("<span>  </span>").append(data.machine));
            let groupH = $("<h3></h3>").append("放线编码 : " + item);
            let gaugeH = $("<h3>规格&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(data.gauge));
            let positiveToleranceH = $("<h3>正公差&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(data.positiveTolerance));
            let negativeToleranceH = $("<h3>负公差&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(data.negativeTolerance));
            let creatorH = $("<h3>排单人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(data.creator));
            let shiftH = $("<h3>班次&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(data.shift));
            let arrangeDataH = $("<h3>生产时间&nbsp;: </h3>").append($("<span></span>").append(data.arrangeDate));

            dataDiv.append(machineH)
                .append(groupH)
                .append(gaugeH)
                .append(positiveToleranceH)
                .append(negativeToleranceH)
                .append(creatorH)
                .append(shiftH)
                .append(arrangeDataH);
            rowDiv.append(qrCodeDiv).append(dataDiv).appendTo("#print-btn-aw");
        });
    } else if (str === "s") {
        let groupNow = data.groupNumber.replace(ra, "").replace(rb, "").split("-");
        let groupNumbers = [];
        for (i = parseInt(groupNow[0]); i <= parseInt(groupNow[1]); i++) {
            let test = i < 10 ? "0" + i : i;
            groupNumbers.push(groupSide + test);
        }
        $.each(groupNumbers, function (index, item) {
            let rowDiv = $("<div class='row' style='page-break-after:always;'></div>");
            let qrCodeDiv = $("<div class='col-lg-6 col-sm-6'></div>").append($("<img>").attr("src", qrCode[index]));
            let dataDiv = $("<div class='col-lg-6 col-ms-6'></div>");

            let machineH = $("<h3>设备型号&nbsp;: </h3>").append($("<span>  </span>").append(data.machine));
            let groupH = $("<h3></h3>").append("收线编码 : " + item);
            let gaugeH = $("<h3>规格&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(data.gauge));
            let positiveToleranceH = $("<h3>正公差&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(data.positiveTolerance));
            let negativeToleranceH = $("<h3>负公差&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(data.negativeTolerance));
            let creatorH = $("<h3>排单人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(data.creator));
            let shiftH = $("<h3>班次&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </h3>").append($("<span></span>").append(data.shift));
            let arrangeDataH = $("<h3>生产时间&nbsp;: </h3>").append($("<span></span>").append(data.arrangeDate));

            dataDiv.append(machineH)
                .append(groupH)
                .append(gaugeH)
                .append(positiveToleranceH)
                .append(negativeToleranceH)
                .append(creatorH)
                .append(shiftH)
                .append(arrangeDataH);
            rowDiv.append(qrCodeDiv).append(dataDiv).appendTo("#print-btn-aw");
        });
    }
}

// ======================== 修改排产 ======================= //

$(document).on("click", "#modify-aw", function () {
    let arrangeId = $(this).attr("arrange-id");
    let gauge = $("#detail-gauge-aw").val();
    let machineName = $("#detail-machine-aw").val();
    let positiveTolerance = $("#detail-positive-tolerance-aw").val();
    let negativeTolerance = $("#detail-negative-tolerance-aw").val();
    let groupNumber = $("#detail-group-number-aw").val();
    let arrangeDate = new Date($("#detail-arrange-time-aw").val()).getTime();
    let detailShift = $("#detail-shift-aw").val();
    let shift = "";
    if (detailShift === "A班") {
        shift = "1";
    } else {
        shift = "0";
    }
    let data = {
        arrangeId,
        gauge,
        positiveTolerance,
        negativeTolerance,
        arrangeDate,
        shift,
        groupNumber,
        machineName
    };

    $.ajax({
        url: serverUrl + "/aw/update",
        contentType: "application/json; charset=utf-8",
        type: "PUT",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        data: JSON.stringify(data),
        success: function (result) {
            if (result.status === 0) {
                Notiflix.Notify.Success("修改成功");
                getAllAwArrange(0, 20);
                $("#aw-arrange-detail-modal").modal("hide");
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
});


/**
 * 从详情中打印
 */
$(document).on("click", "#print-history-aw", function () {
    let awArrangeId = $(this).attr("arrange-id");
    let gauge = $("#detail-gauge-aw").val();
    let positiveTolerance = $("#detail-positive-tolerance-aw").val();
    let negativeTolerance = $("#detail-negative-tolerance-aw").val();
    let shift = $("#detail-shift-aw").val();
    let machine = $("#detail-machine-aw").val();
    let machineNumber = $("#detail-machine-aw").attr("machine-number");
    let arrangeDate = $("#detail-arrange-time-aw").val();
    let creator = $("#detail-creator-aw").val();
    let groupNumber = $("#detail-group-number-aw").val();

    let data = {
        gauge,
        positiveTolerance,
        negativeTolerance,
        creator,
        shift,
        machine,
        arrangeDate,
        groupNumber,
        machineNumber
    };

    $.ajax({
        url: serverUrl + "/aw/" + awArrangeId,
        contentType: "application/json; charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                $("#aw-arrange-detail-modal").modal("hide");
                generateQrCodeByHistoryAw(data, result.data);
                $("#detail-print-modal").modal("show");
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
});