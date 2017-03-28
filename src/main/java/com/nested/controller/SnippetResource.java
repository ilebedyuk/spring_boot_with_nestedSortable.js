package com.nested.controller;

import com.nested.controller.dto.SnippetDTO;
import com.nested.entity.Snippet;
import com.nested.repository.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by elizbor on 01.03.2017.
 */
@RestController
public class SnippetResource {

    @Autowired
    private SnippetRepository snippetRepository;

    @GetMapping(value = "/getTextAjax")
    public ResponseEntity<List<Snippet>> getSnippets() {
        List<Snippet> snippets = snippetRepository.findAll().stream().sorted((s1, s2) -> s1.getOrderSnippet() - s2.getOrderSnippet()).collect(Collectors.toList());
        return new ResponseEntity<>(snippets, HttpStatus.OK);
    }

    @PostMapping(value = "/updateCollapse")
    public ResponseEntity updateCollapse(@RequestBody SnippetDTO snippetDTO) {
        Snippet snippet = snippetRepository.findOne(snippetDTO.getId());
        if (snippet.isCollapsed() != snippetDTO.getCollapse()) {
            snippet.setCollapsed(snippetDTO.getCollapse());
            snippetRepository.save(snippet);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteSnippet")
    public ResponseEntity deleteSnippet(@RequestBody List<String> snippetIds) {
        for (String snippetId : snippetIds) {
            snippetRepository.delete(Long.valueOf(snippetId));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/updateIndent")
    public ResponseEntity updateIndent(@RequestBody String s) {
        Map<String, String> parentChildMap = getMap(s);
        updateAbsoluteIndentAndOrder(parentChildMap);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateAbsoluteIndentAndOrder(Map<String, String> parentChildMap) {
        List<String> currentIdList = new ArrayList<>(parentChildMap.keySet());
        for (Map.Entry<String, String> entry : parentChildMap.entrySet()) {
            Snippet current = snippetRepository.findOne(Long.valueOf(entry.getKey()));

            int pos = currentIdList.indexOf(entry.getKey());
            updateOrder(current, pos);

            if (!entry.getValue().equals("null")) {
                Snippet parent = snippetRepository.findOne(Long.valueOf(entry.getValue()));
                if (current.getIndent() != parent.getIndent() + 1) {
                    current.setIndent(parent.getIndent() + 1);
                    snippetRepository.save(current);
                }
            } else if (current.getIndent() != 0) {
                current.setIndent(0);
                snippetRepository.save(current);
            }
        }
    }

    private void updateOrder(Snippet current, int pos) {
        if (current.getOrderSnippet() != pos) {
            current.setOrderSnippet(pos);
            snippetRepository.save(current);
        }
    }

    private Map<String, String> getMap(String s) {
        String[] s1 = s.replace("\"", "").split("&");
        Map<String, String> parentChildMap = new LinkedHashMap<>();
        Arrays.stream(s1).map(elem -> elem.split("=")).forEach(elem -> parentChildMap.put(getElemId(elem[0]), elem[1]));
        return parentChildMap;
    }

    private String getElemId(String s) {
        Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(s);
        while(m.find()) {
            s = m.group(1);
        }
        return s;
    }
}
