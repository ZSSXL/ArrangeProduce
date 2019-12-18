$("#add-employee").click(function () {
    $("#load-employee-modal").modal("show");
});

$("#add-employee-btn").click(function () {
    let username = $("#username").val();
    let password = $("#password").val();
    let phone = $("#phone").val();
    if (username === "") {
        return true;
    } else if (password === "") {
        return true;
    } else if (phone === "") {
        return true;
    } else {
        let data = {username, phone, password};
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

$(document).on("click", ".delete-employee", function () {
    let userId = $(this).attr("user-id");
    console.log(userId);
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
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
});