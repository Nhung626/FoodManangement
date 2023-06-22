$(document).ready(function () {
    $('#search-input').validate({
        rules: {
            keyword: {
                required: true
            }
        },
        messages: {
            keyword: {
                required: null
            }
        },
        submitHandler: function (form) {
            let keyword = $('#keyword').val();
            let category = $('#category').val();
            let formData = new FormData();
            formData.append("keyword", $('#keyword').val());
            formData.append("category", $('#category').val());
            $.ajax({
                url: `/api/v1/public/search-product`,
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    console.log(response);
                    $('#products').html("");
                    for (let i = 0; i < response.length; i++) {
                        console.log(response);
                        var html = ``;
                        html = ` <div class="col-md-3">
                            <div class="card mb-3">
                                <img src="/api/v1/public/images?imageId=${response[i].imageId}" class="card-img-top rounded mx-auto d-block product-image" style="width: 200px; height: 200px; object-fit: cover;" onerror="this.onerror=null; this.src='https://ih1.redbubble.net/image.485923678.1240/flat,750x,075,f-pad,750x1000,f8f8f8.u4.jpg'">
                            <div class="card-body text-center">
                                <h5 class="card-title"><b>${response[i].name}</b></h5>
                                <p class="card-text text-danger"><b>$${response[i].price}</b></p>
                            <div class="ui-menu-icon">
                                <a class="btn btn-danger"><i class="fa fa-cart-arrow-down"></i></a>
                                <a class="btn btn-danger"><i class="fa fa-heart"></i></a>
                                <a class="btn btn-danger"><i class="fa fa-eye"></i></a>
                            </div>
                            </div></div></div>`;
                        $('#products').append(html);
                    }
                },
                error: function (xhr) {
                    alert("error");
                }
            })
        }
    })
})
