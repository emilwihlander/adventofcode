main = do
  contents <- getContents
  putStr (colitions contents 1 3)

colitions :: [String] -> Int -> Int -> [Bool]
colitions [] _ _ = False
colitions xs r d = [(head xs) !! , colitions (drop d xs) (r  d]