function B = rrefmodp(A, p)

%rrefmodp -- reduced row echelon form (mod p)
%   B = rrefmodp(A,p)
%
%[VdS 2012-aug-2]

%% compile the inverse look-up table.
I = zeros(1,p-1);
Ma = zeros(1,p-1);
for a = (1: p-1)
    Ma = mod(Ma+(1:p-1), p);
    I(a) = find(Ma == 1);
end

%% normalise A to take values in (0:p-1)
A = int32(A);
A = mod(A,p);

[m,n] = size(A);

%% main loop

% set pointers to next pivot
a = 1;
b = 1;

% row permutation
P = (1:m);

while ((a <= m) & (b <= n))
    % if next entry is zero...
    if (A(P(a),b) == 0)
        nzb = find(A(P(a+1:end),b));

       % if column is empty, advance column
       if isempty(nzb)
           b=b+1;
           continue
       end
       
       % else switch rows
       
       a2 = a + nzb(1);
       P([a a2]) = P([a2 a]);
    end
    
    % normalise row
    A(P(a), (b+1:end)) = mod(A(P(a), (b+1:end)) * I(A(P(a),b)), p);
    A(P(a), b) = 1;
    
    % clear remaining rows
    nzb_log = logical(A(:,b));
    nzb_log(P(a)) = 0;
    
    nzb = find(nzb_log);
    for c = 1: length(nzb)
        A(nzb(c), (b+1: end)) = A(nzb(c), (b+1: end)) ...
                                - (A(nzb(c),b) * A(P(a), (b+1:end)));
        A(nzb(c), (b+1: end)) = mod( A(nzb(c), (b+1: end)), p);
    end
    A(nzb_log, b) = 0;

    a = a + 1;
    b = b + 1;
end

B = double(A(P,:));

return





 