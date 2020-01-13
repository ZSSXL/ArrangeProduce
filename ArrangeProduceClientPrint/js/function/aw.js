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
    let creator = $(dom).attr("creator");

    let machine = tr.find("td:eq(1)").text();
    let groupNumber = tr.find("td:eq(2)").text();
    let gauge = tr.find("td:eq(3)").text();
    let positiveTolerance = tr.find("td:eq(4)").text();
    let negativeTolerance = tr.find(("td:eq(5)")).text();
    let arrangeDate = tr.find("td:eq(6)").text();
    let shift = tr.find("td:eq(8)").text();

    $("#machine-name").text(machine);
    $("#group-number-value").text(groupNumber);
    $("#gauge-value").text(gauge);
    $("#positive-tolerance-value").text(positiveTolerance);
    $("#negative-tolerance-value").text(negativeTolerance);
    $("#arrange-time").text(arrangeDate);
    $("#shift-value").text(shift);
    $("#creator-value").text(creator);

}