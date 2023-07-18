var pocketBaseUrl;
var loginPocketBaseInfo = {
    identity: "",
    password: "",
};
$(document).ready(function () {
    alert("hello");

    $.ajax({
        type: "GET",
        url: "/api/v1/get-value",
        success: function (data) {
            pocketBaseUrl= data.pocketBaseHost,
            loginPocketBaseInfo.identity = data.pocketBaseEmail,
            loginPocketBaseInfo.password = data.pocketBasePassword,
            authentication(loginPocketBaseInfo);
            console.log("url1 "+pocketBaseUrl);
        }
    });
    console.log("url 3"+pocketBaseUrl);

});
console.log("url 2"+pocketBaseUrl);

function authentication(loginInfo) {
    $.ajax({
        url: `${pocketBaseUrl}api/admins/auth-with-password`,
        type: "POST",
        data: loginInfo,
        error: function (err) {
            console.log('Error!', err);
        },
        success: function (data) {
            console.log("sss");
            localStorage.setItem("token", data.token);
        }
    });
}
function image(){
}