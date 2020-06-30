import Data.Set
import Data.Maybe
import Data.List
import Data.Map
import System.TimeIt
import System.Environment

type Graph = Data.Map.Map String [String]

merge :: (Eq a) => [a] -> [a] -> [a]
merge a [] = a
merge [] a = a
merge a (x:xs)
    | Prelude.elem x a = merge a xs
    | otherwise        = x : (merge a xs)

linkGraph :: Graph -> String -> [String] -> Graph
linkGraph graph root links = case ( Data.Map.lookup root graph ) of Nothing -> Data.Map.insert root links graph
                                                                    Just a  -> Data.Map.insert root ( merge a links ) graph

mkGraph :: [[String]] -> Graph
mkGraph [] = Data.Map.empty
mkGraph [[]] = Data.Map.empty
mkGraph [ (root:links) ] = Data.Map.fromList [(root, links)]
mkGraph ( (root:links) : tail ) =
    let ng = mkGraph tail
    in linkGraph ng root links

reduceGraph :: Graph -> [String] -> Graph
reduceGraph _ [] = Data.Map.empty
reduceGraph graph selected =
    let newGraph = Data.Map.filterWithKey ( \k _ -> Prelude.elem k selected ) graph
    in Data.Map.map (\v -> Data.List.intersect v selected) newGraph


findHamiltonianR :: Maybe [String] -> [String] -> Graph -> Maybe [String]
findHamiltonianR Nothing _ _ = Nothing
findHamiltonianR (Just links) visited graph
    | Data.Map.null graph = Just visited
    | otherwise           = 
        let bla = [ findHamiltonianR ( Data.Map.lookup x graph ) (x:visited) (Data.Map.delete x graph) | x <- links ]
            bla2 = Data.List.find Data.Maybe.isJust bla
        in Data.Maybe.fromMaybe Nothing bla2

findHamiltonian :: String -> Graph -> Maybe [String]
findHamiltonian node graph = findHamiltonianR (Data.Map.lookup node graph) [node] (Data.Map.delete node graph)

usa = mkGraph [ ["wa","or", "id"],
                  ["or","wa", "id", "nv", "ca"],
                  ["ca","or", "nv", "az"],
                  ["id","wa", "or", "nv", "ut", "wy", "mt"],
                  ["nv","or", "ca", "az", "ut", "id"],
                  ["ut","id", "nv", "az", "co", "wy"],
                  ["az","ca", "nv", "ut", "nm"],
                  ["mt","id", "wy", "sd", "nd"],
                  ["wy","mt", "id", "ut", "co", "ne", "sd"],
                  ["co","wy", "ut", "nm", "ok", "ks", "ne"],
                  ["nm","co", "az", "tx", "ok"],
                  ["nd","mt", "sd", "mn"],
                  ["sd","nd", "mt", "wy", "ne", "ia", "mn"],
                  ["ne","sd", "wy", "co", "ks", "mo", "ia"],
                  ["ks","ne", "co", "ok", "mo"],
                  ["ok","ks", "co", "nm", "tx", "ar", "mo"],
                  ["tx","ok", "nm", "la", "ar"],
                  ["mn","nd", "sd", "ia", "wi"],
                  ["ia","mn", "sd", "ne", "mo", "il", "wi"],
                  ["mo","ia", "ne", "ks", "ok", "ar", "tn", "ky", "il"],
                  ["ar","mo", "ok", "tx", "la", "ms", "tn"],
                  ["la","ar", "tx", "ms"],
                  ["wi","mn", "ia", "il"],
                  ["il","wi", "ia", "mo", "ky", "in"],
                  ["tn","ky", "mo", "ar", "ms", "al", "ga", "nc", "va"],
                  ["ms","tn", "ar", "la", "al"],
                  ["mi","in", "oh"],
                  ["in","mi", "il", "ky", "oh"],
                  ["ky","oh", "in", "il", "mo", "tn", "va", "wv"],
                  ["al","tn", "ms", "fl", "ga"],
                  ["ga","nc", "tn", "al", "fl", "sc"],
                  ["oh","mi", "in", "ky", "wv", "pa"],
                  ["wv","pa", "oh", "ky", "va", "md"],
                  ["ny","pa", "nj", "ct", "ma", "vt"],
                  ["nj","ny", "pa", "de"],
                  ["pa","ny", "nj", "oh", "wv", "md", "de"],
                  ["va","md", "wv", "ky", "tn", "nc", "wdc"],
                  ["nc","va", "tn", "ga", "sc"],
                  ["sc","nc", "ga"],
                  ["fl","ga", "al"],
                  ["me","nh"],
                  ["nh","me", "vt", "ma"],
                  ["vt","nh", "ny", "ma"],
                  ["ma","nh", "vt", "ny", "ct", "ri"],
                  ["ct","ma", "ny", "ri"],
                  ["ri","ma", "ct"],
                  ["de","pa", "md", "nj"],
                  ["md","pa", "wv", "va", "de", "wdc"],
                  ["wdc","md", "va"] ]


eastern = reduceGraph usa ["mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al",
                           "ga", "oh", "wv", "ny", "nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri",
                           "de", "md", "wdc"]

go :: [String] -> IO ()
go [] = timeIt ( putStrLn ( show ( findHamiltonian "wdc" eastern ) ) )
go (h:_) = timeIt ( putStrLn ( show ( findHamiltonian h eastern ) ) )
main = do
    args <- getArgs
    go args
