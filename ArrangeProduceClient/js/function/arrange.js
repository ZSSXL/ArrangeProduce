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

    let data = {gauge, machine, tolerance, weight, arrangeDate, shift};

    console.log(data);
    return data;
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
        url: serverUrl + "/qr",
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