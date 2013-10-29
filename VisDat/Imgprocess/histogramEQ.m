function res = histogramEQ(image, dynamicRange)
    %setup variables
    [x, y] = size(image);
    pixels = x * y;
    hist = zeros(dynamicRange + 1, 1);
    
    %calculate and normalize the histogram
    for i = (0:dynamicRange)
        hist(i + 1) = sum(sum(image == i));
    end
    
    %calculate cumulative distribution
    current = 0;
    cum = zeros(length(hist), 1);
    for i = (1:length(cum))
        current = current + hist(i);
        cum(i) = current;
    end
    cum
    cum = cum / pixels;
    %make blank result image
    res = zeros(size(image));
    
    %map new values from original into res
    for row = (1:size(image, 1))
        for col = (1:size(image, 2))
            res(row, col) = cum(image(row, col) + 1);
        end
    end
end