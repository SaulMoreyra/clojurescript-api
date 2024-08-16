(ns rest-api.db.client
  (:require [cljs.nodejs :as nodejs]))

(def PrismaClient (.-PrismaClient (nodejs/require "@prisma/client")))

(def prisma (PrismaClient.))


(defn get-prisma []
  prisma)