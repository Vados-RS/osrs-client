require 'ostruct'

class ConfigStruct < OpenStruct

  def method_missing(mid, *args)
    mname = mid.id2name
    len = args.length
    if mname && mid != :[]=
      if len != 1
        raise ArgumentError, "wrong number of arguments (#{len} for 1)", caller(1)
      end
      modifiable[new_ostruct_member(mname)] = args[0]
    elsif len == 0 && mid != :[]
      @table[mid]
    else
      raise NoMethodError, "undefined method `#{mid}' for #{self}", caller(1)
    end
  end

end