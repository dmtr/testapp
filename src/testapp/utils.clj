(ns testapp.utils)

(defn parse-int-safe [val default]
  (if (nil? val) default (Integer/parseInt val)))
