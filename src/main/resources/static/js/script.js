$(document).ready(function () {
    $.ajaxSetup({cache: false});

    var ol = $('ol');

    $.ajax({
        type: 'GET',
        url: "/getTextAjax",
        dataType: "json",
        error: function (xhr, status, error) {
            var errorMessage = JSON.parse(xhr.responseText).message;
            alert(errorMessage);
        },
        success: function (JsonData) {
            $.each(JsonData, function (index, data) {
                if (data.indent == 0) {
                    ol.append(getBlock(data));
                } else {
                    var previousSnippetId = JsonData[index-1].snippetId;
                    var previousIndent = JsonData[index-1].indent;

                    if (previousIndent < data.indent) {
                        $('<ol>' + getBlock(data) + '</ol>').appendTo('#menuItem_' + previousSnippetId);
                    } else {
                        for (var i = index-2; i >= 0; i--) {
                            var indent = JsonData[i].indent;
                            if (indent < data.indent) {
                                $('<ol>' + getBlock(data) + '</ol>').appendTo('#menuItem_' + JsonData[i].snippetId);
                                break;
                            }
                        }
                    }
                }
            })
        }

        // Solution for deltaIndent
        // $.each(JsonData, function (index, data) {
        //     if (index == 0 && data.deltaIndent == 0) {
        //         ol.append(getBlock(data));
        //     } else if (data.deltaIndent == 1) {
        //         var previousSnippetId = JsonData[index-1].snippetId;
        //         $('<ol>' + getBlock(data) + '</ol>').appendTo('#menuItem_' + previousSnippetId);
        //     } else if (data.deltaIndent == 0) {
        //         var previousSnippetId = JsonData[index-1].snippetId;
        //         $(getBlock(data)).appendTo('#menuItem_' + previousSnippetId);
        //     } else {
        //
        //         var currentDeltaIndent = data.deltaIndent;
        //
        //         for (var i = index-1; i > 0; i--) {
        //             var deltaIndent = JsonData[i].deltaIndent;
        //             currentDeltaIndent += deltaIndent;
        //             if (currentDeltaIndent == 0) {
        //                 if ((i-1) == 0) {
        //                     ol.append(getBlock(data));
        //                 } else {
        //                     var searchSnippetId = JsonData[i-1].snippetId;
        //                     $(getBlock(data)).appendTo('#menuItem_' + searchSnippetId);
        //                 }
        //                 break;
        //             }
        //         }
        //     }
    });

    $('ol.sortable').nestedSortable({
        forcePlaceholderSize: true,
        handle: 'div',
        helper:	'clone',
        items: 'li',
        opacity: .6,
        placeholder: 'placeholder',
        revert: 250,
        tabSize: 25,
        tolerance: 'pointer',
        toleranceElement: '> div',
        maxLevels: 4,
        isTree: true,
        expandOnHover: 700,
        startCollapsed: false,
        update: function () {
            var list = $(this).nestedSortable('serialize', {startDepthCount: 0});
            $.ajax({
                url: '/updateIndent',
                type: 'POST',
                data: JSON.stringify(list),
                contentType: 'application/json',
                success: function () {
                    console.log("Success");
                },
                error: function () {
                    console.log("Error");
                }
            });
        }
    });

    $(ol).on('click', '.disclose', function() {
        $(this).closest('li').toggleClass('mjs-nestedSortable-collapsed').toggleClass('mjs-nestedSortable-expanded');
        $(this).toggleClass('ui-icon-plusthick').toggleClass('ui-icon-minusthick');
        console.log($(this).attr('data-id'));

        var snippetDTO = {
            "id": $(this).attr('data-id'),
            "collapse" :$(this).hasClass("ui-icon-plusthick")
        };

        $.ajax({
            url: '/updateCollapse',
            type: 'POST',
            data: JSON.stringify(snippetDTO),
            contentType: 'application/json',
            success: function () {
                console.log("Success");
            },
            error: function () {
                console.log("Error");
            }
        });
    });

    $(ol).on('click', '.deleteMenu', function() {
        var parentId = $(this).attr('data-id');
        var children = $('#menuItem_' + parentId).find("ol > li");
        var snippetIds = [];
        snippetIds.push(parentId);
        for (var i = 0; i < children.length; i++) {
            snippetIds.push(children[i].getAttribute("id").replace('menuItem_', ''));
        }
        $('#menuItem_'+parentId).remove();
        $.ajax({
            type: 'DELETE',
            data: JSON.stringify(snippetIds),
            url: "/deleteSnippet",
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            error: function (xhr, status, error) {
                var errorMessage = JSON.parse(xhr.responseText).message;
                console.log(errorMessage);
            },
            success: function () {
                console.log("Success");
            }
        })
    });
});

function getBlock(data) {
    var collapsedClass = data.collapsed ? "mjs-nestedSortable-collapsed" : "mjs-nestedSortable-expanded";
    var collapsedSign = data.collapsed ? "ui-icon-plusthick" : "ui-icon-minusthick";
    return '<li style="display: list-item;"  indent=' + data.indent + ' deltaIndent=' + data.deltaIndent + ' class="mjs-nestedSortable-branch ' + collapsedClass +'" id="menuItem_' + data.snippetId + '">' +
        '<div class="menuDiv">' +
        '<span title="Click to show/hide children" data-id="' +  data.snippetId + '" class="disclose ui-icon ' + collapsedSign + '">' +
        '<span></span>' +
        '</span>' +
        '<span>' +
        '<span title="Click to delete item." data-id="' +  data.snippetId + '" class="deleteMenu ui-icon ui-icon-closethick">'+
        '<span></span>' +
        '</span></span>' +
        '<div id="menuEdit' + data.snippetId + '" class="menuEdit hidden cols">' +
        '<div>' + data.textEN + '</div>' +
        '<div>' + data.textDE + '</div>' +
        '</div></div></li>';
}