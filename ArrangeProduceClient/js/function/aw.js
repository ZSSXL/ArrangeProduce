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
    $("#load-modal-aw").modal("show");
});