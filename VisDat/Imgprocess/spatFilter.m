function res = spatFilter(image, filtersize, type)
    res = zeros(size(image, 1) , size(image, 2));
    filtermid = int32(filtersize/2);
    filterborder = filtermid - 1;

    for i = filtermid:(size(image, 1) - filterborder)
        for j = filtermid:(size(image, 2) - filterborder)
            
            %calculate bounds
            leftBound = i - filterborder;
            rightBound = i + filterborder;
            upBound = j - filterborder;
            downBound = j + filterborder;
            %slice out filtersample
            sample = image(leftBound:rightBound, upBound:downBound);
            
            pixelval = 0;
            if (type == 'avg')
                pixelval = avgfilter(sample);
            elseif (type == 'med')
                pixelval = medfilter(sample);
            end
            res(i , j) = pixelval;
        end
    end
    
    function res = avgfilter(sample)
        avg = 1/(size(sample, 1) * size(sample, 2));
        res = sum(sum(sample*avg));
    end

    function res = medfilter(sample)
        sortedVec = sort(sample(:));
        res = median(sortedVec);
    end
end

