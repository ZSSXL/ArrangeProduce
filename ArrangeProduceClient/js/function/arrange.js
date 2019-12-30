/**
 * 保存按钮点击事件
 */
$("#save-btn").click(function () {
    let data = getDate();
    if (data !== false) {
        saveArrange(data);
    }
});

/**
 * 打印按钮点击事件
 */
$("#print-btn").click(function () {
    let data = getDate();
    if (data !== false) {
        $("#modal-area").load("/sub/print.html");
        saveAndPrint(data);
        $("#load-modal").modal("show");
    }
});

$("#reset-btn").click(function () {
    $('#arrange-date').val("");
    $("#tolerance").val("");
    $('#weight').val("");
    $('#arrange-date').val("");
});

/**
 * 获取表单数据
 * @returns {boolean|{gauge: *, machine: *, shift: *, weight: *, arrangeDate: *, tolerance: *}}
 */
function getDate() {
    let date = $('#arrange-date').val();
    if ($.isEmptyObject(date)) {
        Notiflix.Notify.Failure("请选择生产时间");
        return false;
    }
    let gauge = $("#gauge option:selected").val();
    let machine = $("#machine option:selected").val();
    let tolerance = $("#tolerance").val();
    let weight = $('#weight').val();
    let arrangeDate = new Date($('#arrange-date').val()).getTime();
    let shift = $("input[name='shift']:checked").val();

    return {gauge, machine, tolerance, weight, arrangeDate, shift};
}

/**
 * 保存
 * @param data
 */
function saveArrange(data) {
    $.ajax({
        url: serverUrl + "/arrange",
        contentType: "application/json; charset=utf-8",
        type: "POST",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        data: JSON.stringify(data),
        success: function (result) {
            if (result.status === 0) {
                getAllArrange(0, 20);
                Notiflix.Notify.Success("新建排产任务成功");
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

/**
 * 保存并打印
 * @param data
 */
function saveAndPrint(data) {
    $.ajax({
        url: serverUrl + "/arrange/print",
        contentType: "application/json; charset=utf-8",
        type: "POST",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        data: JSON.stringify(data),
        success: function (result) {
            if (result.status === 0) {
                generateQrCode(data, result.data);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

/**
 * 生成二维码
 * @param data
 * @param qrCode
 */
function generateQrCode(data, qrCode) {
    $("#qr-code").attr("src", qrCode);
    let machine = $("#machine option:selected").text();
    $("#machine-name").text(machine);
    $("#gauge-value").text(data.gauge);
    $("#tolerance-value").text(data.tolerance);
    $("#arrange-time").text($('#arrange-date').val());
    $("#weight-value").text(data.weight);
    if (data.shift === "0") {
        $("#shift-value").text("晚班");
    } else {
        $("#shift-value").text("早班");
    }
}

// 表格相关

/**
 * 全选
 */
$(document).on("click", "#ck", function () {
    const arr = document.getElementsByName('good');
    const ck = document.getElementById('ck');
    for (i in arr) {
        arr[i].checked = ck.checked; // 全选
        //arr[i].checked=!arr[i].checked; 反向全选
    }
});

/**
 * 删除排产信息
 */
$(document).on("click", ".delete-arrange", function () {
    let arrangeId = $(this).attr("arrange-id");
    Notiflix.Confirm.Show("警告", "是否确认删除", "确定", "取消", function () {
        $.ajax({
            url: serverUrl + "/arrange/" + arrangeId,
            contentType: "application/json; charset=utf-8",
            type: "DELETE",
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("token", token);
            },
            success: function (result) {
                if (result.status === 0) {
                    Notiflix.Notify.Success(result.msg);
                    getAllArrange(0, 20);
                } else {
                    Notiflix.Notify.Failure(result.msg);
                }
            }
        });
    });
});

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
 * 推送
 */
$("#push").click(function () {
    const arrangeIdList = [];
    $("input[name='good']:checked").each(function (index, item) {
        arrangeIdList.push($(this).val());
    });
    if (arrangeIdList.length === 0) {
        Notiflix.Notify.Warning("请选勾线要推送的排产信息");
    } else {
        $.ajax({
            url: serverUrl + "/arrange",
            contentType: "application/json; charset=utf-8",
            type: "PUT",
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("token", token);
            },
            data: JSON.stringify(arrangeIdList),
            success: function (result) {
                if (result.status === 0) {
                    Notiflix.Notify.Success("推送成功");
                    getAllArrange(0, 20);
                    socket.send("push a new arrange");
                } else {
                    Notiflix.Notify.Failure(result.msg);
                }
            }
        });
    }
});

// 线规详情

$(document).on("click", ".arrange-detail", function () {
    getArrangeDetail(this);
    $("#arrange-detail-modal").modal("show");
});

function getArrangeDetail(dom) {
    let printStatus = $(dom).attr("print-status");
    let creator = $(dom).attr("creator");
    let createTime = $(dom).attr("create-time");
    let arrangeId = $(dom).attr("arrange-id");

    const tr = $(dom).parents("tr");

    let gauge = tr.find("td:eq(3)").text();
    let tolerance = tr.find("td:eq(4)").text();
    let shift = tr.find("td:eq(7)").text();
    let weight = tr.find("td:eq(5)").text();
    let machine = tr.find("td:eq(2)").attr("machine-name");
    let arrangeDate = tr.find("td:eq(6)").text();
    let push = tr.find("td:eq(8)").text();

    let data = {printStatus, creator, createTime, gauge, tolerance, shift, weight, machine, arrangeDate, push};

    $("#detail-gauge").val(gauge);
    $("#detail-tolerance").val(tolerance);
    $("#detail-shift").val(shift);
    $("#detail-weight").val(weight);
    $("#detail-machine").val(machine);
    $("#detail-arrange-time").val(arrangeDate);
    $("#detail-push").val(push);
    $("#detail-creator").val(creator);
    $("#print-history").attr("arrange-id", arrangeId);
    $("#modify-arrange").attr("arrange-id", arrangeId);

    $("#detail-create-time").val(printTimeFormatComplete(parseInt(createTime)));
    if (printStatus === "0") {
        $("#detail-print").val("未打印");
    } else {
        $("#detail-print").val("已打印");
    }
}

$(document).on("click", ".delete-arrange", function () {
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
                        console.log(machineSort);
                        getAllMachine(machineSort);
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
 * 从详情中打印
 */
$("#print-history").click(function () {
    let arrangeId = $(this).attr("arrange-id");
    let gauge = $("#detail-gauge").val();
    let tolerance = $("#detail-tolerance").val();
    let shift = $("#detail-shift").val();
    let weight = $("#detail-weight").val();
    let machine = $("#detail-machine").val();
    let arrangeDate = $("#detail-arrange-time").val();
    let data = {gauge, tolerance, shift, weight, machine, arrangeDate};
    $.ajax({
        url: serverUrl + "/arrange/" + arrangeId,
        contentType: "application/json; charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                $("#arrange-detail-modal").modal("hide");
                generateQrCodeByHistory(data, result.data);
                $("#detail-print-modal").modal("show");
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
});

/**
 * 从列表中打印
 */
$(document).on("click", ".print-arrange-btn", function () {

    const tr = $(this).parents("tr");

    let arrangeId = $(this).attr("arrange-id");
    let gauge = tr.find("td:eq(3)").text();
    let tolerance = tr.find("td:eq(4)").text();
    let shift = tr.find("td:eq(7)").text();
    let weight = tr.find("td:eq(5)").text();
    let machine = tr.find("td:eq(2)").attr("machine-name");
    let arrangeDate = tr.find("td:eq(6)").text();
    let data = {gauge, tolerance, shift, weight, machine, arrangeDate};
    $.ajax({
        url: serverUrl + "/arrange/" + arrangeId,
        contentType: "application/json; charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                generateQrCodeByHistory(data, result.data);
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
function generateQrCodeByHistory(data, qrCode) {
    $("#qr-code").attr("src", qrCode);
    $("#machine-name").text(data.machine);
    $("#gauge-value").text(data.gauge);
    $("#tolerance-value").text(data.tolerance);
    $("#arrange-time").text(data.arrangeDate);
    $("#weight-value").text(data.weight);
    $("#shift-value").text(data.shift);
}

// ======================== 修改排产 ======================= //
$("#modify-arrange").click(function () {
    let arrangeId = $(this).attr("arrange-id");
    let gauge = $("#detail-gauge").val();
    let tolerance = $("#detail-tolerance").val();
    let weight = $("#detail-weight").val();
    let arrangeDate = new Date($("#detail-arrange-time").val()).getTime();
    let detailShift = $("#detail-shift").val();
    let shift = "";
    if (detailShift === "早班") {
        shift = "1";
    } else {
        shift = "0";
    }
    let machineName = $("#detail-machine").val();
    let data = {arrangeId, gauge, tolerance, weight, arrangeDate, shift, machineName};
    $.ajax({
        url: serverUrl + "/arrange/update",
        contentType: "application/json; charset=utf-8",
        type: "PUT",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        data: JSON.stringify(data),
        success: function (result) {
            if (result.status === 0) {
                Notiflix.Notify.Success("修改成功");
                getAllArrange(0, 20);
                $("#arrange-detail-modal").modal("hide");
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
});