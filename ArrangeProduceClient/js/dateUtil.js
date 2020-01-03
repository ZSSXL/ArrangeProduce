// 格式化时间
/**
 * 时间戳转化为日期格式，精确到日
 * @param timestamp 时间戳
 * @returns {*}
 */
function printTimeFormat(timestamp) {
    const date = new Date(timestamp);

    y = date.getFullYear() + "-";

    // 获取月份(0-11,0代表1月,用的时候记得加上1)
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    d = date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate(); // 获取日(1-31)
    return y + M + d;
}

/**
 * 完整时间 精确到分钟
 * @param timestamp 时间戳
 * @returns {*}
 */
function printTimeFormatComplete(timestamp) {
    const date = new Date(timestamp);

    y = date.getFullYear() + "-";

    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'; // 获取月份(0-11,0代表1月,用的时候记得加上1)
    d = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + " "; // 获取日(1-31)

    h = date.getHours() + ":"; // 获取小时数(0-23)
    m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes(); // 获取分钟数(0-59)
    return y + M + d + h + m;
}
