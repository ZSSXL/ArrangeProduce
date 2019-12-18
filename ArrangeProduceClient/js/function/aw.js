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

$("#machine-setting-aw").click(function () {
    $("#modal-area-aw").load("/sub/machine.html");
    if (machineSort === "annealing") {
        console.log(machineSort);
    } else if (machineSort === "winding") {
        console.log(machineSort);
    }
    getAllMachine(machineSort);
    $("#load-modal-aw").modal("show");
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

// ==================== 添加退火/绕线机排产 ===================== //

/**
 * 保存按钮点击事件
 */
$("#save-btn-aw").click(function () {
    let data = getDate();
    if (data !== false) {
        saveAwArrange(data);
        // Notiflix.Notify.Success("保存成功");
    }
});

/**
 * 打印按钮点击事件
 */
$("#print-btn-aw").click(function () {
    let data = getDate();
    if (data !== false) {
        $("#modal-area").load("/sub/print.html");
        // saveAndPrint(data);
        $("#load-modal").modal("show");
    }
});

/**
 * 重置按钮
 */
$("#reset-btn-aw").click(function () {
    $('#arrange-date-aw').val("");
    $("#tolerance-aw").val("");
    $('#weight-aw').val("");
    $('#arrange-date-aw').val("");
});

function getDate() {
    let date = $('#arrange-date-aw').val();
    if ($.isEmptyObject(date)) {
        Notiflix.Notify.Failure("请选择生产时间");
        return false;
    }
    let gauge = $("#gauge-aw option:selected").val();
    let machine = $("#machine-aw option:selected").val();
    let tolerance = $("#tolerance-aw").val();
    let weight = $('#weight-aw').val();
    let arrangeDate = new Date($('#arrange-date-aw').val()).getTime();
    let shift = $("input[name='shift-aw']:checked").val();
    return {gauge, machine, tolerance, weight, arrangeDate, shift, "sort": machineSort};
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
$("#push-aw").click(function () {
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
                    Notiflix.Notify.Success(result.msg);
                    getAllAwArrange(0, 20);
                } else {
                    Notiflix.Notify.Failure(result.msg);
                }
            }
        });
    }
});


// 线规详情

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

    let gauge = tr.find("td:eq(2)").text();
    let tolerance = tr.find("td:eq(3)").text();
    let shift = tr.find("td:eq(6)").text();
    let weight = tr.find("td:eq(4)").text();
    let machine = tr.find("td:eq(1)").text();
    let arrangeDate = tr.find("td:eq(5)").text();
    let push = tr.find("td:eq(7)").text();

    let data = {printStatus, creator, createTime, gauge, tolerance, shift, weight, machine, arrangeDate, push};

    $("#detail-gauge-aw").val(gauge);
    $("#detail-tolerance-aw").val(tolerance);
    $("#detail-shift-aw").val(shift);
    $("#detail-weight-aw").val(weight);
    $("#detail-machine-aw").val(machine);
    $("#detail-arrange-time-aw").val(arrangeDate);
    $("#detail-push-aw").val(push);
    $("#detail-creator-aw").val(creator);
    $("#print-history-aw").attr("aw-arrange-id", arrangeId);

    $("#detail-create-time-aw").val(printTimeFormatComplete(parseInt(createTime)));
    if (printStatus === "0") {
        $("#detail-print-aw").val("未打印");
    } else {
        $("#detail-print-aw").val("已打印");
    }
}