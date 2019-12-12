// 格式化时间
function printTimeFormat(timestamp) {
    const date = new Date(timestamp);

    y = date.getFullYear() + "-";

    // 获取月份(0-11,0代表1月,用的时候记得加上1)
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    d = date.getDate() + " "; // 获取日(1-31)
    return y + M + d;
}

function printTimeFormatSimple(timestamp) {
    var date = new Date(timestamp);

    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'; // 获取月份(0-11,0代表1月,用的时候记得加上1)
    d = date.getDate() + " "; // 获取日(1-31)

    h = date.getHours() + ":"; // 获取小时数(0-23)
    m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes(); // 获取分钟数(0-59)
    return M + d + h + m;
}
