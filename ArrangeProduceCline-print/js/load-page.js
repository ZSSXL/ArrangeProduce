const totalPages = 0; // 总页数
const currentArrangePage = 0; // 保存分页总记录数


$("#load-arrange").click(function () {
    localStorage.setItem("arrangeCurrentPage", "arrange");
    $("#load-area").load("/arrange.html");
    $(this).attr("class", "active");
    $("#load-aw").removeAttr("class");
    machineSort = "draw";
});

$("#load-aw").click(function () {
    localStorage.setItem("arrangeCurrentPage", "aw");
    $("#load-area").load("/aw.html");
    $(this).attr("class", "active");
    $("#load-arrange").removeAttr("class");
});

function currentPage() {
    let arrangeCurrentPage = localStorage.getItem("arrangeCurrentPage");
    $("#load-" + arrangeCurrentPage).attr("class", "active");
    $("#load-area").load("\\" + arrangeCurrentPage + ".html");
}
