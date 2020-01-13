$(document).on("click", ".arrange-print", function () {
    let arrangeId = $(this).attr("arrange-id");
    getArrangeQrCode(arrangeId);
    getData(this);
    $("#load-modal").modal("show");
});

function getArrangeQrCode(arrangeId) {
    $.ajax({
        url: serverUrl + "/arrange/" + arrangeId,
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                $("#qr-code").attr("src", result.data);
                getAllArrange(0, 20);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function getData(dom) {
    const tr = $(dom).parents("tr");
    let rawMaterials = $(dom).attr("raw-materials");
    let creator = $(dom).attr("creator");

    let machine = tr.find("td:eq(1)").text();
    let gauge = tr.find("td:eq(2)").text();
    let positiveTolerance = tr.find("td:eq(3)").text();
    let negativeTolerance = tr.find("td:eq(4)").text();
    let inletDiameter = tr.find("td:eq(5)").text();
    let weight = tr.find("td:eq(6)").text();
    let arrangeTime = tr.find("td:eq(7)").text();
    let shift = tr.find("td:eq(9)").text();

    $("#machine-name").text(machine);
    $("#gauge-value").text(gauge);
    $("#inlet-diameters-value").text(inletDiameter);
    $("#positive-tolerance-value").text(positiveTolerance);
    $("#negative-tolerance-value").text(negativeTolerance);
    $("#arrange-time").text(arrangeTime);
    $("#shift-value").text(shift);
    $("#raw-materials-value").text(rawMaterials);
    $("#creator-value").text(creator);

}