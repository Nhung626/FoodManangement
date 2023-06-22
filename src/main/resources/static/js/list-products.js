$(document).ready(function () {
    $('.close').click(function () {
        $('#popup').hide();
    });

    let pageSize = parseInt($('#pageSize').text());
    loadProducts(0, pageSize);

    $('#previous-link').click(function (event) {
        event.preventDefault();
        let pageNo = parseInt($('#pageNo').text()) - 1;
        let pageSize = parseInt($('#pageSize').text());
        loadProducts(pageNo - 1, pageSize);
    });

    $('#next-link').click(function (event) {
        event.preventDefault();
        let pageNo = parseInt($('#pageNo').text()) - 1;
        let pageSize = parseInt($('#pageSize').text());
        loadProducts(pageNo + 1, pageSize);
    });
});

function loadProducts(pageNo, pageSize) {
    $.ajax({
        url: `/api/v1/products?pageNo=${pageNo}&pageSize=${pageSize}`,
        type: 'GET',
        processData: false,
        contentType: false,
        success: function (response) {
            console.log(response);
            let products = response.content;
            let pageNumber = parseInt(response.pageable.pageNumber);
            let pageSize = parseInt(response.pageable.pageSize);
            let totalPages = parseInt(response.totalPages);
            displayProducts(products, pageNumber, pageSize);
            updatePagination(pageNumber, totalPages);
        },
        error: function (xhr, status, error) {
            showPopupError('Error!', xhr.responseJSON.message)
        }
    });
}

function displayProducts(products, pageNo, pageSize) {
    let html = ``;
    for (let i = 0; i < products.length; i++) {
        html += `<tr>
                  <td> ${i + 1 + pageNo * pageSize} </td>
                  <td>${products[i].name}</td>
                  <td>${products[i].price}</td>
                  <td>
                  <button type="button" class="btn btn-primary">Edit</button>
                  <button type="button" class="btn btn-danger">Delete</button</td>
                  </tr>`
    }
    $('#listProducts tbody').html(html);
}

function updatePagination(pageNo, totalPages) {
    if (pageNo <= 0) {
        $("#previous-link").hide();
    } else {
        $("#previous-link").show();
    }

    if (pageNo >= totalPages - 1) {
        $("#next-link").hide();
    } else {
        $("#next-link").show();
    }
    let pageLink = document.getElementById("pageNo");
    pageLink.innerHTML = pageNo + 1;
}

function showPopupError(title, message) {
    $('#popup .modal-header h2').text(title);
    $('#popup .modal-header h2').css({
        'color': 'red'
    })
    $('#popup .modal-body h3').text(message);
    $('#popup').show();
}