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