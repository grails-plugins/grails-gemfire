<html>
<body>
    <h1>Query Results</h1>
    
    <H2>Query: ${query}</H2>
    <ul>
    <g:each var="result" in="${results}">
    <li>${result}</li>
    </g:each>
    </ul>
</body>
</html>