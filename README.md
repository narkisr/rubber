# Intro

Rubber is a collection of Elasticsearch common functions, snapshot and index management built on top of [spandex](https://github.com/mpenet/spandex)

# Usage

Basic connectivity:

```clojure
(require '[rubber.node :refer :all])

(connect {:hosts ["localhost:9200"])

(check) ; check cluster health and connectivity
```

Core functions:

```clojure
(require '[rubber.core :refer :all])

(create-index :people {:mappings {:person {:age "long"}}})

(def id (create :people :person {:age 21}))

(get :people :person id)

(delete :people :person id)
```

Index manipulation

```clojure
(list-indices)

(refresh-index :people); synch index for search

(delete-index :people)

(mappings :people :person); get type mappings
```

[![Build Status](https://travis-ci.org/narkisr/rubber.png)](https://travis-ci.org/narkisr/rubber)

# Copyright and license

Copyright [2018] [Ronen Narkis]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
