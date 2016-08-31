(ns ctim.generators.schemas.bundle-generators
  (:require [clj-momo.lib.time :as time]
            [schema.core :as s]
            [schema-tools.core :as st]
            [clojure.test.check.generators :as gen]
            [ctim.schemas.common :as schemas-common]
            [ctim.schemas.bundle :as p]
            [ctim.generators.common
             :refer [complete leaf-generators maybe]
             :as common]
            [ctim.generators.id :as gen-id]
            [ctim.generators.schemas.actor-generators :refer [gen-actor]]
            [ctim.generators.schemas.campaign-generators :refer [gen-campaign]]
            [ctim.generators.schemas.coa-generators :refer [gen-coa]]
            [ctim.generators.schemas.exploit-target-generators :refer [gen-exploit-target]]
            [ctim.generators.schemas.feedback-generators :refer [gen-feedback]]
            [ctim.generators.schemas.incident-generators :refer [gen-incident]]
            [ctim.generators.schemas.indicator-generators :refer [gen-indicator]]
            [ctim.generators.schemas.judgement-generators :refer [gen-judgement]]
            [ctim.generators.schemas.sighting-generators :refer [gen-sighting]]
            [ctim.generators.schemas.ttp-generators :refer [gen-ttp]]
            [schema-generators.generators :as seg]))

(def object-keys [:actors
                  :campaigns
                  :coas
                  :exploit-targets
                  :feedbacks
                  :incidents
                  :indicators
                  :judgements
                  :sightings
                  :ttps])

(s/defschema BaseNewBundle
  (apply st/dissoc p/NewBundle object-keys))

(s/defschema BaseStoredBundle
  (apply st/dissoc p/StoredBundle object-keys))

(defn merge-entities [[s id actors campaigns coas
                       exploit-targets feedbacks
                       incidents indicators judgements
                       sightings ttps]]
  (cond-> (dissoc s :id)
    id (assoc :id id)
    actors (assoc :actors actors)
    campaigns (assoc :campaigns campaigns)
    coas (assoc :coas coas)
    exploit-targets (assoc :exploit-targets exploit-targets)
    feedbacks (assoc :feedbacks feedbacks)
    incidents (assoc :incidents incidents)
    indicators (assoc :indicators indicators)
    judgements (assoc :judgements judgements)
    sightings (assoc :sightings sightings)
    ttps (assoc :ttps ttps)))


(def gen-bundle
  (gen/fmap
   merge-entities
   (gen/tuple (seg/generator BaseStoredBundle leaf-generators)
              (gen-id/gen-short-id-of-type :bundle)
              (maybe (gen/vector gen-actor 0 1))
              (maybe (gen/vector gen-campaign 0 1))
              (maybe (gen/vector gen-coa 0 1))
              (maybe (gen/vector gen-exploit-target 0 1))
              (maybe (gen/vector gen-feedback 0 1))
              (maybe (gen/vector gen-incident 0 1))
              (maybe (gen/vector gen-indicator 0 1))
              (maybe (gen/vector gen-judgement 0 1))
              (maybe (gen/vector gen-sighting 0 1))
              (maybe (gen/vector gen-ttp 0 1)))))

(defn gen-new-bundle_ [gen-id]
  (gen/fmap
   merge-entities
   (gen/tuple (seg/generator BaseNewBundle leaf-generators)
              gen-id
              (maybe (gen/vector gen-actor 0 1))
              (maybe (gen/vector gen-campaign 0 1))
              (maybe (gen/vector gen-coa 0 1))
              (maybe (gen/vector gen-exploit-target 0 1))
              (maybe (gen/vector gen-feedback 0 1))
              (maybe (gen/vector gen-incident 0 1))
              (maybe (gen/vector gen-indicator 0 1))
              (maybe (gen/vector gen-judgement 0 1))
              (maybe (gen/vector gen-sighting 0 1))
              (maybe (gen/vector gen-ttp 0 1)))))

(def gen-new-bundle
  (gen-new-bundle_
   (maybe (gen-id/gen-short-id-of-type :bundle))))

(def gen-new-bundle-with-id
  (gen-new-bundle_
   (gen-id/gen-short-id-of-type :bundle)))
