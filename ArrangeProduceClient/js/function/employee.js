$("#add-employee").click(function () {
    $("#load-employee-modal").modal("show");
});

/**
 * 添加员工
 */
$("#add-employee-btn").click(function () {
    let username = $("#username").val();
    let password = $("#password").val();
    let phone = $("#phone").val();
    let employeeNumber = $("#number").val();
    let sex = $("input[name='sex']:checked").val();

    if (username === "") {
        return true;
    } else if (password === "") {
        return true;
    } else if (phone === "") {
        return true;
    } else if (employeeNumber === "") {
        return true;
    } else {
        let data = {username, phone, password, employeeNumber, sex};
        $.ajax({
            url: serverUrl + "/user",
            contentType: "application/json; charset=utf-8",
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("token", token);
            },
            type: "POST",
            data: JSON.stringify(data),
            success: function (result) {
                if (result.status === 0) {
                    Notiflix.Notify.Success(result.msg);
                    $("#load-employee-modal").modal("hide");
                    getAllEmployee(0, 20);
                } else {
                    Notiflix.Notify.Failure(result.msg);
                }
            }
        });
    }
});

/**
 * 删除员工
 */
$(document).on("click", ".delete-employee", function () {
    let userId = $(this).attr("user-id");
    Notiflix.Confirm.Show("警告", "是否确认删除", "确定", "取消", function () {
        $.ajax({
            url: serverUrl + "/user/" + userId,
            contentType: "application/json; charset=utf-8",
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("token", token);
            },
            type: "DELETE",
            success: function (result) {
                if (result.status === 0) {
                    Notiflix.Notify.Success("删除成功");
                    getAllEmployee(0, 20);
                } else {
                    Notiflix.Notify.Failure(result.msg);
                }
            }
        });
    });
});

/**
 * 打印员工编码
 */
$(document).on("click", ".print-employee", function () {
    let userId = $(this).attr("user-id");

    let username = $(this).parents("tr").find("td:eq(1)").text();

    $.ajax({
        url: serverUrl + "/user/print/" + userId,
        contentType: "application/json; charset=utf-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                generateQrCode(username, result.data, result.msg);
                $("#employee-print-modal").modal("show");
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
});

/**
 * 生成二维码
 *
 * @param username 用户名
 * @param qrCode 返回结果
 * @param number 编号
 */
function generateQrCode(username, qrCode, number) {
    // 删除员工编码的第一个字符，如 1001 转换后为: 001
    number = number.substring(0, 0) + number.substring(1, number.length);
    $("#qr-code").attr("src", qrCode);
    $("#employee-name").text(username);
    $("#employee-number").text(number);
}

$(document).on("click", ".detail-employee", function () {
    let userId = $(this).attr("user-id");

    $.ajax({
        url: serverUrl + "/user/" + userId,
        contentType: "application/json; charset=utf-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                showEmployeeDetail(result.data);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
    $("#employee-detail-modal").modal("show");
});

/**
 * 显示员工详情
 *
 * @param data 详情数据
 */
function showEmployeeDetail(data) {
    $("#detail-employee-name").val(data.username);
    $("#detail-employee-number").val(data.employeeNumber);
    $("#detail-phone").val(data.phone);
    if (data.sex === "1") {
        $("#detail-sex").val("男").attr("sex-tag", "1");
    } else {
        $("#detail-sex").val("女").attr("sex-tag", "0");
    }

    $("#modify-employee").attr("user-id", data.employeeId);
    $("#print-detail").attr("user-id", data.employeeId);
}

/**
 * 详情打印
 */
$("#print-detail").click(function () {
    let userId = $(this).attr("user-id");
    let name = $("#detail-employee-name").val();

    $.ajax({
        url: serverUrl + "/user/print/" + userId,
        contentType: "application/json; charset=utf-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                $("#employee-detail-modal").modal("hide");
                generateQrCode(name, result.data, result.msg);
                $("#employee-print-modal").modal("show");
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });

});

/**
 * 修改员工信息
 */
$("#modify-employee").click(function () {
    let employeeId = $(this).attr("user-id");
    let username = $("#detail-employee-name").val();
    let phone = $("#detail-phone").val();
    let employeeNumber = $("#detail-employee-number").val();
    let department = $("#detail-department").val();
    let post = $("#detail-post").val();
    let sex = $("#detail-sex").attr("sex-tag");

    let data = {employeeId, employeeNumber, username, phone, department, post, sex};

    Notiflix.Confirm.Show("警告", "是否确认修改", "确定", "取消", function () {
        $.ajax({
            url: serverUrl + "/user/info",
            contentType: "application/json; charset=utf-8",
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("token", token);
            },
            type: "PUT",
            data: JSON.stringify(data),
            success: function (result) {
                if (result.status === 0) {
                    Notiflix.Notify.Success("修改成功");
                    getAllEmployee(0, 20);
                    $("#employee-detail-modal").modal("hide");
                } else {
                    Notiflix.Notify.Failure(result.msg);
                }
            }
        });
    });
});
