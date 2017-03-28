## Demo project on the movement of text blocks with the choice of parent and child

Technologies: nestedSortable.js, Spring Boot, Spring Data JPA, Spring MVC, JQuery etc

## Motivation

There were no examples of using nestedSortable.js library with the ability to save the results of moving blocks to the database

## Logics

At the heart of the application is the entity named "Snippet", which has next fields:
* Long snippetId - id
* String textDE - text in Germany
* String textEN - text in English
* int indent - nesting level
* boolean isCollapsed - flag for collapse or extend
* int orderSnippet - order in snippet's tree

User can move each snippet in the tree and the result of the movening will save to the database

## Note

For initial block rendering is used:

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

To transfer the state of the tree to the controller is used update function:

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

