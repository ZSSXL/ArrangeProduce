var totalPages = 0; // 总页数
var currentArrangePage = 0; // 保存分页总记录数


$("#load-arrange").click(function () {
    localStorage.setItem("arrangeCurrentPage", "arrange");
    $("#load-area").load("/arrange.html");
    $(this).attr("class", "active");
    $("#load-aw").removeAttr("class");
    $("#load-setting").removeAttr("class");
    $("#load-employee").removeAttr("class");
    machineSort = "draw";
    getAllArrange(0, 20);
    getAllGaugeSelected("arrange");
    getAllMachineDraw();
});

$("#load-aw").click(function () {
    localStorage.setItem("arrangeCurrentPage", "aw");
    $("#load-area").load("/aw.html");
    $(this).attr("class", "active");
    $("#load-arrange").removeAttr("class");
    $("#load-setting").removeAttr("class");
    $("#load-employee").removeAttr("class");
    machineSort = "annealing";
    getAllAwArrange(0, 20);
    getAllMachineAnnealing();
    getAllGaugeSelected("aw");
});

$("#load-setting").click(function () {
    localStorage.setItem("arrangeCurrentPage", "settings");
    $("#load-area").load("/settings.html");
    $(this).attr("class", "active");
    $("#load-aw").removeAttr("class");
    $("#load-arrange").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-employee").click(function () {
    localStorage.setItem("arrangeCurrentPage", "employee");
    $("#load-area").load("/employee.html");
    $(this).attr("class", "active");
    $("#load-aw").removeAttr("class");
    $("#load-arrange").removeAttr("class");
    $("#load-setting").removeAttr("class");
    getAllEmployee(0, 20);
});

function currentPage() {
    let arrangeCurrentPage = localStorage.getItem("arrangeCurrentPage");
    if (arrangeCurrentPage === null) {
        $("#load-area").load("/arrange.html");
        machineSort = "draw";
        getAllMachineDraw();
        getAllGaugeSelected("arrange");
        $("#load-arrange").attr("class", "active");
    } else {
        $("#load-" + arrangeCurrentPage).attr("class", "active");
        if (arrangeCurrentPage === "arrange") {
            machineSort = "draw";
            getAllArrange(0, 20);
            getAllGaugeSelected("arrange");
            getAllMachineDraw();
        } else if (arrangeCurrentPage === "aw") {
            // 默认获取退火机设备
            machineSort = "annealing";
            getAllAwArrange(0, 20);
            getAllMachineAnnealing();
            getAllGaugeSelected("aw");
        } else if (arrangeCurrentPage === "employee") {
            getAllEmployee(0, 20);
        }
        $("#load-area").load("\\" + arrangeCurrentPage + ".html");
    }
}

function getAllGaugeSelected(choice) {
    $.ajax({
        url: serverUrl + "/gauge",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                if (choice === "arrange") {
                    showGaugeToArrange(result.data);
                } else if (choice === "aw") {
                    showGaugeToAw(result.data);
                }
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function getAllMachineDraw() {
    $.ajax({
        url: serverUrl + "/machine/draw",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                showDraw(result.data);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function showGaugeToArrange(data) {
    $("#gauge").empty();
    $.each(data, function (index, item) {
        const option = $("<option></option>").append(item.gauge);
        option.attr("value", item.gauge);
        $("#gauge").append(option);
    })
}

function showGaugeToAw(data) {
    $("#gauge").empty();
    $.each(data, function (index, item) {
        const option = $("<option></option>").append(item.gauge);
        option.attr("value", item.gauge);
        $("#gauge-aw").append(option);
    })
}

function showDraw(data) {
    $("#machine").empty();
    $.each(data, function (index, item) {
        const option = $("<option></option>").append(item.machineName).attr("value", item.machineNumber);
        $("#machine").append(option);
    })
}

/**
 * 获取所有的小拉机排产数据
 */
function getAllArrange(page, size) {
    $.ajax({
        url: serverUrl + "/arrange",
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
            let checkboxId = $("<td></td>").append($("<input type='checkbox' name='good'>").attr("value", item.arrangeId));
            let numTd = $("<td scope='row'></td>").append(index + 1);
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
            let pushTd = $("<td></td>");
            if (item.push === "yes") {
                pushTd.append("已推送");
            } else {
                pushTd.append("未推送");
            }
            let detailBtn = $("<button class='btn btn-outline-info arrange-detail'>详情</button>").attr({
                "print-status": item.status,
                "creator": item.creator,
                "create-time": item.createTime,
                "arrange-id": item.arrangeId
            });
            let deleteBtn = $("<button class='btn btn-outline-danger delete-arrange'>删除</button>").attr("arrange-id", item.arrangeId);
            let btnTd = $("<td></td>").append(detailBtn).append(deleteBtn);
            $("<tr></tr>").append(checkboxId)
                .append(numTd)
                .append(machineTd)
                .append(gaugeTd)
                .append(toleranceTd)
                .append(weightTd)
                .append(arrangeDateTd)
                .append(shiftTd)
                .append(pushTd)
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


// ---------------------- aw ------------------------ //

function getAllMachineAnnealing() {
    $.ajax({
        url: serverUrl + "/machine/annealing",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                showMachineToAw(result.data);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function showMachineToAw(data) {
    $("#machine-aw").empty();
    $.each(data, function (index, item) {
        const option = $("<option></option>").append(item.machineName).attr("value", item.machineNumber);
        $("#machine-aw").append(option);
    })
}


// ========================== 获取所有退火/绕线机数据 ============================ //

/**
 * 获取所有的小拉机排产数据
 */
function getAllAwArrange(page, size) {
    $.ajax({
        url: serverUrl + "/aw",
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
            let checkboxId = $("<td></td>").append($("<input type='checkbox' name='good-aw'>").attr("value", item.awArrangeId));
            let numTd = $("<td scope='row'></td>").append(index + 1);
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
            let pushTd = $("<td></td>");
            if (item.push === "yes") {
                pushTd.append("已推送");
            } else {
                pushTd.append("未推送");
            }
            let detailBtn = $("<button class='btn btn-outline-info aw-arrange-detail'>详情</button>").attr({
                "print-status": item.status,
                "creator": item.creator,
                "create-time": item.createTime,
                "arrange-id": item.arrangeId
            });
            let deleteBtn = $("<button class='btn btn-outline-danger delete-arrange-aw'>删除</button>").attr("aw-arrange-id", item.awArrangeId);
            let btnTd = $("<td></td>").append(detailBtn).append(deleteBtn);
            $("<tr></tr>").append(checkboxId)
                .append(numTd)
                .append(machineTd)
                .append(gaugeTd)
                .append(toleranceTd)
                .append(weightTd)
                .append(arrangeDateTd)
                .append(shiftTd)
                .append(pushTd)
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

// ======================== 员工页面操作 ========================== //

function getAllEmployee(page, size) {
    $.ajax({
        url: serverUrl + "/user",
        data: "page=" + page + "&size=" + size,
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                analyticalUser(result);
                build_page_info_user(result);
                build_page_li_user(result);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function analyticalUser(result) {
    $("#show-all-employees").empty();
    const data = result.data.content;
    if (data.length === 0) {
        $("#no-message-user").css("display", "none");
    } else {
        $("#no-message-user").css("display", "none");
        $("#nav-message-user").css("display", "inline-block");
        $.each(data, function (index, item) {
            let numTd = $("<td></td>").append(index + 1);
            let usernameTd = $("<td></td>").append(item.userName);
            let phoneTd = $("<td></td>").append(item.phone);
            let createTimeTd = $("<td></td>").append(printTimeFormatComplete(item.createTime));
            let deleteBtn = $("<button class='btn btn-outline-danger delete-employee'>删除</button>").attr("user-id", item.userId);
            let btnTd = $("<td></td>").append(deleteBtn);

            $("<tr></tr>").append(numTd)
                .append(usernameTd)
                .append(phoneTd)
                .append(createTimeTd)
                .append(btnTd)
                .appendTo("#show-all-employees");
        });
    }
}

function build_page_info_user(result) {
    // 有则清空
    $("#page_info_area_user").empty();

    var pageInfo = result.data;
    $("#page_info_area_user").append(" 当前第 <strong>" + (pageInfo.number + 1) + "</strong> 页,总共" + pageInfo.totalPages +
        "页,共" + pageInfo.totalElements + "条记录");
    totalPages = pageInfo.totalPages;
    currentArrangePage = pageInfo.number;
}

function build_page_li_user(result) {
    $("#page-ul-user").empty();

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
            getAllEmployee(0, 20);
        });
        prePageLi.click(function () {
            getAllEmployee(currentArrangePage - 1, 20);
        });
    }

    ul.append(firstPageLi).append(prePageLi);
    // ------------------------------------------  //

    if (totalPages >= 5) {
        if (currentArrangePage >= 2) {
            for (i = currentArrangePage - 2; i <= currentArrangePage + 2; i++) {
                var numLi = $("<li class='page-item'></li>").append($("<a class='page-link page-jump-user'></a>").append(i + 1));
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
                let numLi = $("<li class='page-item'></li>").append($("<a class='page-link page-jump-user'></a>").append(i + 1));
                if (currentArrangePage === i) {
                    numLi.addClass("active");
                }
                ul.append(numLi);
            }
        }
    } else {
        for (i = 0; i < totalPages; i++) {
            let numLi = $("<li class='page-item'></li>").append($("<a class='page-link page-jump-user'></a>").append(i + 1));
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
            getAllEmployee(currentArrangePage + 1, 20);
        });
        // 跳转最后一页点击事件
        lastPageLi.click(function () {
            getAllEmployee(totalPages - 1, 20);
        });
    }

    ul.append(nextPageLi).append(lastPageLi).appendTo("#page-ul-user");
}