let totalPages = 0; // 总页数
let currentArrangePage = 0; // 保存分页总记录数


$("#load-arrange").click(function () {
    localStorage.setItem("arrangeCurrentPage", "arrange");
    $("#load-area").load("/arrange.html");
    $(this).attr("class", "active");
    $("#load-aw").removeAttr("class");
    getAllArrange(0, 20);
});

$("#load-aw").click(function () {
    localStorage.setItem("arrangeCurrentPage", "aw");
    $("#load-area").load("/aw.html");
    $(this).attr("class", "active");
    $("#load-arrange").removeAttr("class");
    getAllAwArrange(0, 20);
});

function currentPage() {
    let arrangeCurrentPage = localStorage.getItem("arrangeCurrentPage");
    if (arrangeCurrentPage === "arrange") {
        getAllArrange(0, 20);
    } else if (arrangeCurrentPage === "aw") {
        getAllAwArrange(0, 20);
    }
    $("#load-" + arrangeCurrentPage).attr("class", "active");
    $("#load-area").load("\\" + arrangeCurrentPage + ".html");
}



// ========================== 获取所有的小拉机排产数据 ============================ //

function getAllArrange(page, size) {
    $.ajax({
        url: serverUrl + "/arrange/employee",
        data: "page=" + page + "&size=" + size,
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                analyticalArrange(result);
                build_page_info(result);
                build_page_li(result);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function analyticalArrange(result) {
    $("#arrange-history-area").empty();
    const data = result.data.content;
    if (data.length === 0) {
        $("#no-message").css("display", "none");
    } else {
        $("#no-message").css("display", "none");
        $("#nav-message").css("display", "inline-block");
        $.each(data, function (index, item) {
            let numTd = $("<th scope='row'></th>").append(index + 1);
            let machineTd = $("<td></td>").append(item.machine);
            let gaugeTd = $("<td></td>").append(item.gauge);
            let toleranceTd = $("<td></td>").append(item.tolerance);
            let weightTd = $("<td></td>").append(item.weight);
            let arrangeDateTd = $("<td></td>").append(printTimeFormat(item.arrangeDate));
            let shiftTd = $("<td></td>");
            if (item.shift === "1") {
                shiftTd.append("早班");
            } else {
                shiftTd.append("晚班");
            }
            let detailBtn = $("<button>打印</button>").attr({
                "print-status": item.status,
                "creator": item.creator,
                "create-time": item.createTime,
                "arrange-id": item.arrangeId
            });
            let printStatusTd = $("<td></td>");
            if (item.status === 1) {
                printStatusTd.append("已打印");
                detailBtn.attr("class", "btn btn-lg btn-outline-info aw-arrange-print");
            } else if (item.status === 0) {
                printStatusTd.append("未打印");
                detailBtn.attr("class", "btn btn-lg btn-outline-warning aw-arrange-print");
            }
            let btnTd = $("<td></td>").append(detailBtn);
            $("<tr></tr>").append(numTd)
                .append(machineTd)
                .append(gaugeTd)
                .append(toleranceTd)
                .append(weightTd)
                .append(arrangeDateTd)
                .append(printStatusTd)
                .append(shiftTd)
                .append(btnTd).appendTo("#arrange-history-area");
        });
    }
}

function build_page_info(result) {
    // 有则清空
    $("#page_info_area").empty();

    var pageInfo = result.data;
    $("#page_info_area").append(" 当前第 <strong>" + (pageInfo.number + 1) + "</strong> 页,总共" + pageInfo.totalPages +
        "页,共" + pageInfo.totalElements + "条记录");
    totalPages = pageInfo.totalPages;
    currentArrangePage = pageInfo.number;
}

function build_page_li(result) {
    $("#page-ul").empty();

    var ul = $("<ul class='pagination pagination-sm justify-content-center'></ul>");
    // 首页
    var firstPageLi = $("<li class='page-item'></li>").append($("<a class='page-link' href='#'></a>").append("首页"));
    // 前一页
    var prePageLi = $("<li class='page-item'></li>")
        .append($("<a class='page-link' href='#' aria-label='Previous'></a>")
            .append($("<span aria-hidden='true'>&laquo;</span>")).append($("<span class='sr-only'>Previous</span>")));
    // 判断是否还有上一页，没有则disable
    if (result.data.first === true) {
        firstPageLi.addClass("disabled");
        prePageLi.addClass("disabled");
    } else {
        firstPageLi.click(function () {
            getAllArrange(0, 20);
        });
        prePageLi.click(function () {
            getAllArrange(currentArrangePage - 1, 20);
        });
    }

    ul.append(firstPageLi).append(prePageLi);
    // ------------------------------------------  //

    if (totalPages >= 5) {
        if (currentArrangePage >= 2) {
            for (i = currentArrangePage - 2; i <= currentArrangePage + 2; i++) {
                var numLi = $("<li class='page-item'></li>").append($("<a class='page-link page-jump'></a>").append(i + 1));
                if (currentArrangePage === i) {
                    numLi.addClass("active");
                }
                if (totalPages === i) {
                    break;
                }
                ul.append(numLi);
            }
        } else {
            for (i = 0; i < 5; i++) {
                let numLi = $("<li class='page-item'></li>").append($("<a class='page-link page-jump'></a>").append(i + 1));
                if (currentArrangePage === i) {
                    numLi.addClass("active");
                }
                ul.append(numLi);
            }
        }
    } else {
        for (i = 0; i < totalPages; i++) {
            let numLi = $("<li class='page-item'></li>").append($("<a class='page-link page-jump'></a>").append(i + 1));
            if (currentArrangePage === i) {
                numLi.addClass("active");
            }
            ul.append(numLi);
        }
    }

    // ------------------------------------------ //

    // 后一页
    var nextPageLi = $("<li class='page-item'></li>")
        .append($("<a class='page-link' href='#' aria-label='Next'></a>")
            .append($("<span aria-hidden='true'>&raquo;</span>")).append($("<span class='sr-only'>Next</span>")));
    // 尾页
    var lastPageLi = $("<li class='page-item'></li>").append($("<a class='page-link' href='#'></a>").append("尾页"));
    // 判断时候还有下一页，没有则disable
    if (result.data.last === true) {
        nextPageLi.addClass("disabled");
        lastPageLi.addClass("disabled");
    } else {
        // 跳转下一页点击事件
        nextPageLi.click(function () {
            getAllArrange(currentArrangePage + 1, 20);
        });
        // 跳转最后一页点击事件
        lastPageLi.click(function () {
            getAllArrange(totalPages - 1, 20);
        });
    }

    ul.append(nextPageLi).append(lastPageLi).appendTo("#page-ul");
}

$(document).on("click", ".page-jump", function () {
    var page = $(this).text();
    getAllArrange(page - 1, 20);
});



// ========================== 获取所有退火/绕线机数据 ============================ //

/**
 * 获取所有的小拉机排产数据
 */
function getAllAwArrange(page, size) {
    $.ajax({
        url: serverUrl + "/aw/employee",
        data: "page=" + page + "&size=" + size,
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                analyticalAwArrange(result);
                build_page_info_aw(result);
                build_page_li_aw(result);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function analyticalAwArrange(result) {
    $("#aw-arrange-history-area").empty();
    const data = result.data.content;
    if (data.length === 0) {
        $("#no-message-aw").css("display", "none");
    } else {
        $("#no-message-aw").css("display", "none");
        $("#nav-message-aw").css("display", "inline-block");
        $.each(data, function (index, item) {
            let numTd = $("<th scope='row'></th>").append(index + 1);
            let machineTd = $("<td></td>").append(item.machine);
            let gaugeTd = $("<td></td>").append(item.gauge);
            let toleranceTd = $("<td></td>").append(item.tolerance);
            let weightTd = $("<td></td>").append(item.weight);
            let arrangeDateTd = $("<td></td>").append(printTimeFormat(item.arrangeDate));
            let shiftTd = $("<td></td>");
            if (item.shift === "1") {
                shiftTd.append("早班");
            } else {
                shiftTd.append("晚班");
            }
            let detailBtn = $("<button>打印</button>").attr({
                "print-status": item.status,
                "creator": item.creator,
                "create-time": item.createTime,
                "arrange-id": item.arrangeId
            });
            let printStatusTd = $("<td></td>");
            if (item.status === 1) {
                printStatusTd.append("已打印");
                detailBtn.attr("class", "btn btn-lg btn-outline-info aw-arrange-print");
            } else if (item.status === 0) {
                printStatusTd.append("未打印");
                detailBtn.attr("class", "btn btn-lg btn-outline-warning aw-arrange-print");
            }
            let btnTd = $("<td></td>").append(detailBtn);
            $("<tr></tr>").append(numTd)
                .append(machineTd)
                .append(gaugeTd)
                .append(toleranceTd)
                .append(weightTd)
                .append(arrangeDateTd)
                .append(printStatusTd)
                .append(shiftTd)
                .append(btnTd).appendTo("#aw-arrange-history-area");
        });
    }
}

function build_page_info_aw(result) {
    // 有则清空
    $("#page_info_area_aw").empty();

    var pageInfo = result.data;
    $("#page_info_area_aw").append(" 当前第 <strong>" + (pageInfo.number + 1) + "</strong> 页,总共" + pageInfo.totalPages +
        "页,共" + pageInfo.totalElements + "条记录");
    totalPages = pageInfo.totalPages;
    currentArrangePage = pageInfo.number;
}

function build_page_li_aw(result) {
    $("#page-ul-aw").empty();

    var ul = $("<ul class='pagination pagination-sm justify-content-center'></ul>");
    // 首页
    var firstPageLi = $("<li class='page-item'></li>").append($("<a class='page-link' href='#'></a>").append("首页"));
    // 前一页
    var prePageLi = $("<li class='page-item'></li>")
        .append($("<a class='page-link' href='#' aria-label='Previous'></a>")
            .append($("<span aria-hidden='true'>&laquo;</span>")).append($("<span class='sr-only'>Previous</span>")));
    // 判断是否还有上一页，没有则disable
    if (result.data.first === true) {
        firstPageLi.addClass("disabled");
        prePageLi.addClass("disabled");
    } else {
        firstPageLi.click(function () {
            getAllAwArrange(0, 20);
        });
        prePageLi.click(function () {
            getAllAwArrange(currentArrangePage - 1, 20);
        });
    }

    ul.append(firstPageLi).append(prePageLi);
    // ------------------------------------------  //

    if (totalPages >= 5) {
        if (currentArrangePage >= 2) {
            for (i = currentArrangePage - 2; i <= currentArrangePage + 2; i++) {
                var numLi = $("<li class='page-item'></li>").append($("<a class='page-link page-jump-aw'></a>").append(i + 1));
                if (currentArrangePage === i) {
                    numLi.addClass("active");
                }
                if (totalPages === i) {
                    break;
                }
                ul.append(numLi);
            }
        } else {
            for (i = 0; i < 5; i++) {
                let numLi = $("<li class='page-item'></li>").append($("<a class='page-link page-jump-aw'></a>").append(i + 1));
                if (currentArrangePage === i) {
                    numLi.addClass("active");
                }
                ul.append(numLi);
            }
        }
    } else {
        for (i = 0; i < totalPages; i++) {
            let numLi = $("<li class='page-item'></li>").append($("<a class='page-link page-jump-aw'></a>").append(i + 1));
            if (currentArrangePage === i) {
                numLi.addClass("active");
            }
            ul.append(numLi);
        }
    }

    // ------------------------------------------ //

    // 后一页
    var nextPageLi = $("<li class='page-item'></li>")
        .append($("<a class='page-link' href='#' aria-label='Next'></a>")
            .append($("<span aria-hidden='true'>&raquo;</span>")).append($("<span class='sr-only'>Next</span>")));
    // 尾页
    var lastPageLi = $("<li class='page-item'></li>").append($("<a class='page-link' href='#'></a>").append("尾页"));
    // 判断时候还有下一页，没有则disable
    if (result.data.last === true) {
        nextPageLi.addClass("disabled");
        lastPageLi.addClass("disabled");
    } else {
        // 跳转下一页点击事件
        nextPageLi.click(function () {
            getAllAwArrange(currentArrangePage + 1, 20);
        });
        // 跳转最后一页点击事件
        lastPageLi.click(function () {
            getAllAwArrange(totalPages - 1, 20);
        });
    }

    ul.append(nextPageLi).append(lastPageLi).appendTo("#page-ul-aw");
}

$(document).on("click", ".page-jump-aw", function () {
    let page = $(this).text();
    getAllAwArrange(page - 1, 20);
});