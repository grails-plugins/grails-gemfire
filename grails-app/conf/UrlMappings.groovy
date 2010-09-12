class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/remove/$key" {
            controller = 'simpleTemplate'
            action = 'removeFromCache'
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
