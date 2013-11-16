function res = thresh(image, T)
    res = zeros(size(image, 1), size(image, 2));
    for y = 1:size(image, 2)
        for x = 1:size(image, 1) 
            res(x, y) = image(x, y) > T;
        end
    end
end