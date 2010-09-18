<html>
<body>
    <h1>Cache</h1>
    <h2>Statistics</h2>
    <g:render template="cacheStats" bean="${statistics}"/>
    
    <h2>Entries</h2>
    <g:render template="cacheEntries" collection="${cacheEntries}"/>
</body>
</html>