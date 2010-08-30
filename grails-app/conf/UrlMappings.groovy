class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/add/$key/$value" {
            controller = 'simpleTemplate'
            action = 'addToCache'
        }
        
        "/displayCache" {
            controller = 'simpleTemplate'
            action = 'displayCache'
        }
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
