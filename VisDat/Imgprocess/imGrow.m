function res = imGrow(image, seeds, T)
    res = zeros(size(image));
    seedInd = 1;
    while(seedInd < length(seeds))
        seed = [seeds(seedInd), seeds(seedInd + 1)];
        res = growRegion(image, res, seed, T);
        seedInd = seedInd + 2;
    end
end

function res = growRegion(image, resImage, seed, T)
    %4-neighbourhood growth and check
    toBeChecked = seed;
    checkInd = 1;
    oX = seed(1);
    oY = seed(2);
    resImage(oY, oX) = 1;
    while (length(toBeChecked) > checkInd)
        x = toBeChecked(checkInd);
        y = toBeChecked(checkInd + 1);
        
        for i = (-1:1)
            for j = (-1:1)
                if (i == j || i == -j)
                    continue;
                end
                newX = x + j;
                newY = y + i;
                %bounds check
                if ~(newY < 1 || newY >= size(image, 1) || newX < 1 || newX >= size(image, 2))
                    if (abs(image(newY, newX) - image(oY, oX)) < T && resImage(newY, newX) == 0)
                        toBeChecked = [toBeChecked, newX, newY];
                        resImage(newY, newX) = 1;
                    end
                end
            end
        end
        checkInd = checkInd + 2;
    end
    res = resImage;
end