(ns labels.core
  (:require  [rum.core :as rum]))

(defonce *state (atom {:name "John doe"
                       :number "0731231234"
                       :pencils 12
                       :large 3
                       :medium 4
                       :small 5}))

(defn wrap-field [id]
  (fn [e]
    (swap! *state #(assoc % id (.. e -target -value)))))


(rum/defc inputs
  [state]
  [:div
   [:div
    [:label "Name:"]
    [:input {:type "text" :value (:name state) :on-change (wrap-field :name)}]]
   [:div
    [:label "Number:"]
    [:input {:type "text" :value (:number state) :on-change (wrap-field :number)}]]
   [:div
    [:label "Total Pencil Labels:"]
    [:input {:type "number" :value (:pencils state) :on-change (wrap-field :pencils)}]]
   [:div
    [:label "Total Small Labels:"]
    [:input {:type "number" :value (:small state) :on-change (wrap-field :small)}]]
   [:div
    [:label "Total Medium Labels:"]
    [:input {:type "number" :value (:medium state) :on-change (wrap-field :medium)}]]
   [:div
    [:label "Total Large Labels:"]
    [:input {:type "number" :value (:large state) :on-change (wrap-field :large)}]]
   ])


(rum/defc labels [coll]
  [:div.labels
   (map-indexed (fn [i l] [:div.label {:key i} l]) coll)])


(rum/defc label [class nm nr]
  [:div {:class class}
   [:div.title nm]
   [:div nr]])

(def pencil-label (partial label "pencil"))
(def small-label (partial label "small"))
(def medium-label (partial label "medium"))
(def large-label (partial label "large"))

(rum/defc app < rum/reactive []
  (let [{n :name nr :number :as state :keys [pencils small medium large]} (rum/react *state)]
    [:div
     [:div.no-print
      (inputs state)
      [:hr]]
     [:div.printable
      (labels (repeat pencils (pencil-label n nr)))
      (labels (repeat small (small-label n nr)))
      (labels (repeat medium (medium-label n nr)))
      (labels (repeat large (large-label n nr)))
      #_(map #(rum/with-key (pencil-label n nr) %) (range pencils))]]))



(rum/mount (app) (js/document.getElementById "app"))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
