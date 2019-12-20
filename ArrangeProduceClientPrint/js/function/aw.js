$(document).on("click", ".aw-arrange-print", function () {
    let awArrangeId = $(this).attr("aw-arrange-id");
    getAwArrangeQrCode(awArrangeId);
    getData(this);
    $("#load-modal-aw").modal("show");
});

function getAwArrangeQrCode(awArrangeId) {
    $.ajax({
        url: serverUrl + "/aw/" + awArrangeId,
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                $("#qr-code-aw").attr("src", result.data);
                getAllAwArrange(0, 20);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function getData(dom) {
    const tr = $(dom).parents("tr");
    let machine = tr.find("td:eq(0)").text();
    let gauge = tr.find("td:eq(1)").text();
    let tolerance = tr.find("td:eq(2)").text();
    let weight = tr.find("td:eq(3)").text();
    let arrangeTime = tr.find("td:eq(4)").text();
    let shift = tr.find("td:eq(6)").text();

    $("#machine-name-aw").text(machine);
    $("#gauge-value-aw").text(gauge);
    $("#tolerance-value-aw").text(tolerance);
    $("#weight-value-aw").text(weight);
    $("#arrange-time-aw").text(arrangeTime);
    $("#shift-value-aw").text(shift);
}