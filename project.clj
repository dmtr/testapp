(defproject testapp "0.1.0-SNAPSHOT"
  :description "test app"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring/ring-core "1.9.2"]
                 [ring/ring-jetty-adapter "1.9.2"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.6.2"]
                 [com.datomic/dev-local "0.9.232"]
                 [com.datomic/client-cloud "0.8.105"]
                 [org.clojure/tools.logging "1.1.0"]
                 [ring/ring-mock "0.4.0"]
                 [org.clojure/data.json "2.2.0"]
                 [org.clojure/clojurescript "1.10.844"
                  :exclusions [org.apache.ant/ant]]
                 [hiccup "1.0.4"]
                 [reagent "1.0.0"]]
  :main ^:skip-aot testapp.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :cljsbuild {
    :builds [{:source-paths ["src-cljs"]
              :compiler {:output-to "resources/public/js/main.js"
                         :optimizations :whitespace
                         :pretty-print true}}]}
  :plugins [[cider/cider-nrepl "0.24.0"] [lein-cljsbuild "1.1.8"]])
